package com.example.misis2.ei;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EIRepository extends JpaRepository<EI, Integer> {

    @Query(value = "SELECT ins_ei(:r_name, :r_short_name, :r_code)", nativeQuery = true)
    Integer ins_ei(@Param("r_name") String r_name, @Param("r_short_name") String r_short_name, @Param("r_code") Integer r_code);

    @Query(value = "SELECT del_ei(:r_id_ei)", nativeQuery = true)
    Integer del_ei(@Param("r_id_ei") Integer r_id_ei);

    @Query(value = "SELECT update_ei(:r_id_ei, :r_name, :r_short_name, :r_code)", nativeQuery = true)
    Integer update_ei(@Param("r_id_ei") Integer r_id_ei, @Param("r_name") String r_name, @Param("r_short_name") String r_short_name, @Param("r_code") Integer r_code);

}
