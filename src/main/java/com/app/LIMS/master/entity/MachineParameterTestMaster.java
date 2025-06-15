package com.app.LIMS.master.entity;

import jakarta.persistence.*;

@Entity
public class MachineParameterTestMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MachineMaster machine;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MachineMaster getMachine() {
		return machine;
	}

	public void setMachine(MachineMaster machine) {
		this.machine = machine;
	}

	public TestMaster getTest() {
		return test;
	}

	public void setTest(TestMaster test) {
		this.test = test;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
    @Column(nullable = true)
    private String labcode; // <-- Added labcode field


    public String getLabcode() {
		return labcode;
	}

	public void setLabcode(String labcode) {
		this.labcode = labcode;
	}
	@ManyToOne
    private TestMaster test;

    @Column(nullable = false)
    private String parameter;

    // getters and setters
}