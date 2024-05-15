package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        // createEntityManagerFactory 메소드 파라미터로 persistenceUnitName이 필요한데 이것은 persistence.xml에 persistence-unit name으로 설정해뒀다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        // 트랜잭션 얻어오기
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");
            em.persist(member);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close(); // EntityManager가 내부적으로 데이너 커넥션을 물고 동작하기 때문에 반드시 닫아줘야 한다.
        }
        emf.close();
    }
}
