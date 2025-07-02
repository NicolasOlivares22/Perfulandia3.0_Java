package com.vededores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vededores.models.Vendedor;

public interface VendedorRepository extends JpaRepository<Vendedor, Integer> {

}
