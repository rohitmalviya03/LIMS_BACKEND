package com.app.LIMS.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
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

import okhttp3.OkHttpClient;
import okhttp3.*;
import okhttp3.MediaType;


import org.json.JSONObject;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import okhttp3.Credentials;
@RestController

@RequestMapping("/api/bills")
public class BillController {
    private final BillRepository billRepo;
    private final PatientRepository patientRepo;
    private final TestSampleRepository raisedTestRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String RAZORPAY_KEY = "YOUR_KEY_ID";
    private final String RAZORPAY_SECRET = "YOUR_SECRET_KEY";

    
    
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
        
        Patient patient = patientRepo.findByIdAndLabcode(patientId,String.valueOf(payload.get("labcode")))
        	    .orElseThrow(() -> new RuntimeException("Patient not found"));
        	bill.setPatient(patient);
        bill.setDiscountPercent(discountPercent);
        bill.setTaxPercent(taxPercent);
        bill.setTotal(total);
        bill.setLabcode(String.valueOf(payload.get("labcode")));
        try {
            bill.setItemsJson(objectMapper.writeValueAsString(items));
        } catch (Exception ignored) {}

        return billRepo.save(bill);
    }

    @GetMapping("/history")
    public List<Bill> getBills(@RequestParam Long patientId, @RequestParam String labcode) {
        Patient patient = patientRepo.findByIdAndLabcode(patientId,labcode).orElse(null);
        return billRepo.findByPatientAndLabcode(patient,labcode);
    }
    
    //payment
   

    // 2. Verify Razorpay Payment and Update Bill
    @PostMapping("/razorpay/verify")
    public ResponseEntity<?> verifyRazorpayPayment(@RequestBody Map<String, Object> data) {
        String razorpayOrderId = (String) data.get("razorpay_order_id");
        String razorpayPaymentId = (String) data.get("razorpay_payment_id");
        String razorpaySignature = (String) data.get("razorpay_signature");
        Long billId = Long.valueOf(data.get("billId").toString());

        // Signature verification
        String payload = razorpayOrderId + "|" + razorpayPaymentId;
        String actualSignature;
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(RAZORPAY_SECRET.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(payload.getBytes());
            actualSignature = new String(Base64.getEncoder().encode(hash));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Signature verification failed");
        }

        if (!actualSignature.equals(razorpaySignature)) {
            return ResponseEntity.status(400).body("Invalid signature");
        }

        // Mark bill as paid
        billRepo.findById(billId).ifPresent(bill -> {
            bill.setPaid(true);
            billRepo.save(bill);
        });

        return ResponseEntity.ok("Payment verified and bill updated");
    }

}