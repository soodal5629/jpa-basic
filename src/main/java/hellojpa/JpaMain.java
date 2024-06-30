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
            /*
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember = " + findMember.getName());
            // update
            findMember.setName("updatedHellooA");
            // em.remove(findMember); -> 삭제
            Member member = new Member();
            //member.setId(101L);
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
            }*/
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member1 = new Member();
            member1.setName("member1");
            member1.setTeam(team);
            em.persist(member1);
            Member findMember1 = em.find(Member.class, member1.getId());
            Team findTeam = findMember1.getTeam();
            System.out.println("findTeam.getName() = " + findTeam.getName());
            member1.setHomeAddress(new Address("homeCity", "street1", "10000"));
            // 값 타입 컬렉션
            member1.getFavoriteFoods().add("치킨");
            member1.getFavoriteFoods().add("족발");

            member1.getAddressHistory().add(new Address("old1", "street1", "10000"));
            member1.getAddressHistory().add(new Address("old2", "street1", "10000"));

            // 값 타입 컬렉셕 수정 (치킨 -> 한식)
            member1.getFavoriteFoods().remove("치킨");
            member1.getFavoriteFoods().add("한식");

            member1.getAddressHistory().remove(new Address("old1","street1", "10000"));
            member1.getAddressHistory().add(new Address("newCity1","street1", "10000"));

//            em.flush();
//            em.clear();

            Member findMember = em.find(Member.class, member1.getId());
            List<Member> members = findMember.getTeam().getMembers();
            for (Member member : members) {
                System.out.println("member.getName() = " + member.getName());
            }
            System.out.println("!!!!!!");
            em.flush();
            em.clear();

            // 즉시 로딩할 경우 해당 쿼리에서 n+1 문제 발생
            //List<Member> resultList = em.createQuery("select m from Member m", Member.class).getResultList();
            Child child1 = new Child();
            Child child2 = new Child();
            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            // 페이징 처리
            List<Member> resultList = em.createQuery("select m from Member m order by m.id desc", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();
            System.out.println("result.size = " + resultList.size());
            for (Member member : resultList) {
                System.out.println("find member = " + member);
            }
            em.persist(parent);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close(); // EntityManager가 내부적으로 데이너 커넥션을 물고 동작하기 때문에 반드시 닫아줘야 한다.
            emf.close();
        }
        //emf.close();
    }
}
