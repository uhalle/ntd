package com.notodolist;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	
    List<Task> findByUserIdAndGroupId(Long userId, Long groupId);
    
    List<Task> findByUserId(Long userId);

}
