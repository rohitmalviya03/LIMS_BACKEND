package com.app.LIMS.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.LIMS.Repository.BillRepository;
import com.app.LIMS.Repository.PatientRepository;
import com.app.LIMS.Repository.TestSampleRepository;
import com.app.LIMS.entity.Bill;
import com.app.LIMS.entity.Patient;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    private final BillRepository billRepo;
    private final PatientRepository patientRepo;
    private final TestSampleRepository raisedTestRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BillController(BillRepository billRepo, PatientRepository patientRepo, TestSampleRepository raisedTestRepo) {
        this.billRepo = billRepo;
        this.patientRepo = patientRepo;
        this.raisedTestRepo = raisedTestRepo;
    }

    @PostMapping
    public Bill create(@RequestBody Map<String, Object> payload) {
        Long patientId = ((Number) payload.get("patientId")).longValue();
        List<Map<String, Object>> items = (List<Map<String, Object>>) payload.get("items");
        Double discountPercent = payload.get("discountPercent") == null ? 0.0 : Double.valueOf(payload.get("discountPercent").toString());
        Double taxPercent = payload.get("taxPercent") == null ? 0.0 : Double.valueOf(payload.get("taxPercent").toString());
        Double total = payload.get("total") == null ? 0.0 : Double.valueOf(payload.get("total").toString());

        // Mark raised tests as billed
        for (Map<String, Object> item : items) {
            if ("test".equals(item.get("type")) && item.get("id") != null) {
                Long raisedTestId = ((Number) item.get("id")).longValue();
                raisedTestRepo.findById(raisedTestId).ifPresent(rt -> {
                    rt.setBilled(true);
                    raisedTestRepo.save(rt);
                });
            }
        }

        Bill bill = new Bill();
       // bill.setPatient(patientRepo.findById(patientId));
        
        Patient patient = patientRepo.findById(patientId)
        	    .orElseThrow(() -> new RuntimeException("Patient not found"));
        	bill.setPatient(patient);
        bill.setDiscountPercent(discountPercent);
        bill.setTaxPercent(taxPercent);
        bill.setTotal(total);
        try {
            bill.setItemsJson(objectMapper.writeValueAsString(items));
        } catch (Exception ignored) {}

        return billRepo.save(bill);
    }

    @GetMapping("/history")
    public List<Bill> getBills(@RequestParam Long patientId) {
        Patient patient = patientRepo.findById(patientId).orElse(null);
        return billRepo.findByPatient(patient);
    }
    
    
}