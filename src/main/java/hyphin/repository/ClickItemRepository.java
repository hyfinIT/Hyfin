package hyphin.repository;

import hyphin.model.click.ClickItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickItemRepository extends JpaRepository<ClickItem, Long> {
}
