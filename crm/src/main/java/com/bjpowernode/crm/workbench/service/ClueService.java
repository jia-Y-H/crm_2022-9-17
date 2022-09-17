package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;

import javax.servlet.http.HttpServletRequest;

public interface ClueService {
    Boolean save(Clue clue);

    Clue detail(String id);

    Boolean unbund(String id);

    boolean bund(String cid, String[] aids);

    Boolean convert(String clueId, Tran t, String createBy );
}
