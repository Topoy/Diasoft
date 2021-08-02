package main.repository;

import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM t_user tu " +
            "JOIN t_audit ta ON tu.user_id = ta.user_id " +
            "WHERE ta.user_id NOT IN (" +
            "SELECT user_id FROM t_audit ta " +
            "WHERE action_type = 2 " +
            "GROUP BY user_id)",
    nativeQuery = true)
    List<User> getUsersWithoutMailChecking();

    @Query(value = "SELECT name, MAX(ta.action_date), tat.expire_date FROM t_user tu " +
            "JOIN t_audit ta ON tu.user_id = ta.user_id " +
            "JOIN t_access_token tat ON ta.audit_id = tat.audit_id " +
            "GROUP BY ta.user_id, tat.expire_date " +
            "HAVING MAX(ta.action_date) > tat.expire_date", nativeQuery = true )
    List<User> getUsersWithExpiredAccess();

    @Query(value = "SELECT * FROM t_user tu " +
            "JOIN t_audit ta ON tu.user_id = ta.user_id " +
            "WHERE ta.user_id NOT IN (" +
            "SELECT user_id FROM t_audit ta " +
            "Where action_type = 3 " +
            "GROUP BY user_id)", nativeQuery = true)
    List<User> getUsersWithoutLogin();

    @Query(value = "SELECT name, tu.user_id, tu.mail, tu.pass FROM t_user tu " +
            "JOIN t_audit ta ON tu.user_id = ta.user_id " +
            "GROUP BY tu.user_id " +
            "ORDER BY COUNT(IF(ta.action_type = 5, 1, NULL)) " +
            "DESC", nativeQuery = true)
    List<User> getTopUsersWithPassFail();
}
