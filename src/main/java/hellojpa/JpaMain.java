package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // createEntityManagerFactory 메소드 파라미터로 persistenceUnitName이 필요한데 이것은 persistence.xml에 persistence-unit name으로 설정해뒀다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        // 트랜잭션 얻어오기
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember = " + findMember.getName());
            // update
            findMember.setName("updatedHellooA");
            // em.remove(findMember); -> 삭제
            Member member = new Member();
            member.setId(101L);
            member.setName("HelloD");
            // 영속 상태가 되는데 befor/after 주석 사이에 before와 after 주석 사이에 아무런 sql도 찍히지 않고 추후에 insert 문이 찍힌다!
            System.out.println("==== BEFORE ====");
            em.persist(member);
            System.out.println("==== AFTER ====");
            Member member101 = em.find(Member.class, 101L);
            System.out.println("member101 = " + member101.getName());

            List<Member> resultList = em.createQuery("select m from Member m", Member.class)
                    // jpql은 페이징 처리도 가능
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();
            for (Member memberEntity : resultList) {
                System.out.println("member = " + memberEntity.getName());
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close(); // EntityManager가 내부적으로 데이너 커넥션을 물고 동작하기 때문에 반드시 닫아줘야 한다.
        }
        emf.close();
    }
}
