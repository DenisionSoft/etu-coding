package com.example.misis2.ei;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EIService {
    private static EIRepository eiRepository;

    @Autowired
    public EIService(EIRepository eiRepository) {
        this.eiRepository = eiRepository;
    }

    public static Integer ins_ei(String r_name) {
        return eiRepository.ins_ei(r_name);
    }
    public static Integer del_ei(Integer r_id_ei) {
        return eiRepository.del_ei(r_id_ei);
    }
    public static Integer update_ei(Integer r_id_ei, String r_name) {
        return eiRepository.update_ei(r_id_ei, r_name);
    }

    public static EI select_ei(Integer r_id_ei) {
        try {
            EI ei = eiRepository.findById(r_id_ei).get();
            return ei;
        } catch (Exception e) {
            return new EI("null");
        }
    }
}
