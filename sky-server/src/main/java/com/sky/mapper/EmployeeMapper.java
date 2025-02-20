package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import com.sky.mapper.Provider.EmployeeProvider;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);
    /**
     * 插入员工
     * @param employee
     * @return
     */

    @Insert("insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) values (#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);


    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    @SelectProvider(type = EmployeeProvider.class, method = "pageQuery")
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新员工状态
     * @param employee 员工对象
     * @return 影响行数
     */
    @UpdateProvider(type = EmployeeProvider.class, method = "update")
    @AutoFill(value = OperationType.UPDATE)
    int update(Employee employee);

    /**
     * 根据ID查询员工
     * @param id 员工id
     * @return 员工信息
     */
    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);



}
