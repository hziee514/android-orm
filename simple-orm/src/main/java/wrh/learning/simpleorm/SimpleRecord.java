package wrh.learning.simpleorm;

/**
 * @author bruce.wu
 * @date 2018/6/6
 */
public class SimpleRecord {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean delete() {
        return SimpleDao.delete(this);
    }

    public void save() {
        SimpleDao.save(this);
    }

    public void update() {
        SimpleDao.update(this);
    }

}
