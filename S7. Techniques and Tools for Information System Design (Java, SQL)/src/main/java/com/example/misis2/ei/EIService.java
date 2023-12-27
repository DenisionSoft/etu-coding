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
        String r_short_name = r_name;
        if (r_name.length() > 3) {
            r_short_name = r_name.substring(0, 3);
        }
        Integer r_code = r_short_name.hashCode();
        return eiRepository.ins_ei(r_name, r_short_name, r_code);
    }
    public static Integer del_ei(Integer r_id_ei) {
        return eiRepository.del_ei(r_id_ei);
    }
    public static Integer update_ei(Integer r_id_ei, String r_name) {
        String r_short_name = r_name;
        if (r_name.length() > 3) {
            r_short_name = r_name.substring(0, 3);
        }
        Integer r_code = r_short_name.hashCode();
        return eiRepository.update_ei(r_id_ei, r_name, r_short_name, r_code);
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
