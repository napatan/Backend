package com.taekwondogym.backend.dto;

import java.util.List;

public class CartDTO {
	private Long id;
    private String username;
    private List<CartItemDTO> items;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<CartItemDTO> getItems() {
		return items;
	}
	public void setItems(List<CartItemDTO> items) {
		this.items = items;
	}
    
}
