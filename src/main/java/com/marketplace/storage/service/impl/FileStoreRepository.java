package com.marketplace.storage.service.impl;


import com.marketplace.common.repository.BaseRepository;
import com.marketplace.storage.domain.FileStoreBO;
import org.springframework.stereotype.Repository;

@Repository
interface FileStoreRepository extends BaseRepository<FileStoreBO, String> {
}
