package com.cloud.filoft.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cloud.filoft.model.File;


@Repository
public interface FileRepository extends JpaRepository<File,String> {
	
	@Query("SELECT file FROM File file WHERE LOWER(file.emailid) = LOWER(:emailid)")
    public ArrayList<File> retrieveUserFiles(@Param("username") String emailid);

}
