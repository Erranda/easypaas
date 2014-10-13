package com.withinet.opaas.model;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withinet.opaas.model.domain.Download;
import com.withinet.opaas.model.domain.Permission;

public interface DownloadRepository extends JpaRepository <Download, Long> {
	
}
