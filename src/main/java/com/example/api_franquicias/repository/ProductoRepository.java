
package com.example.api_franquicias.repository;

import com.example.api_franquicias.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findBySucursalId(Long sucursalId);

    Optional<Producto> findFirstBySucursalIdOrderByStockDescIdAsc(
            Long sucursalId
    );


}
