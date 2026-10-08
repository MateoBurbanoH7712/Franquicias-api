
package com.example.api_franquicias.repository;

import com.example.api_franquicias.entity.Franquicia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranquiciaRepository extends JpaRepository<Franquicia, Long> {

}
