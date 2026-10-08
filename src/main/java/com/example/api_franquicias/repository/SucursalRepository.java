
package com.example.api_franquicias.repository;

import com.example.api_franquicias.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    List<Sucursal> findByFranquiciaId(Long franquiciaId);

}
