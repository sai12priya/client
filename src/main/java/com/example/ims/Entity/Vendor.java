package com.example.ims.Entity;

import org.springframework.stereotype.Component;

@Component
public class Vendor {
	private Long id;
    private String vendorName;
    private String companyName;
    @Override
	public String toString() {
		return "Vendor [id=" + id + ", vendorName=" + vendorName + ", companyName=" + 
	companyName + ", email=" + email;
				
	}
	private String email;
    private String phone;
    private Product product;
    private String address;
    private String status;
    
    public Vendor() {}
	public Vendor(String name, String companyName, Product product,String email, String phone,
			String address, String status) {
		super();
//		this.id = id;
		this.vendorName = name;
		this.companyName = companyName;
		this.email = email;
		this.phone = phone;
		this.product = product;
		this.address = address;
		this.status = status;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String name) {
		this.vendorName = name;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
}
