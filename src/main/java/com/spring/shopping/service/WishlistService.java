package com.spring.shopping.service;

import com.spring.shopping.DTO.WishlistDTO;

import java.util.List;

public interface WishlistService {

    WishlistDTO addToWishlist(Long userId, Long productId);

    void removeFromWishlist(Long wishlistId);

    List<WishlistDTO> getUserWishlist(Long userId);

}
