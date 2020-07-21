import com.kpi.dao.EvidentFileDAO;
import com.kpi.dao.ItemDAO;
import com.kpi.pojo.EvidentFile;
import com.kpi.pojo.Item;
import com.kpi.pojo.User;
import com.kpi.dao.UserDAO;
import com.kpi.service.UserService;
import com.kpi.spring.util.SpringContextUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.ws.Service;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestPro {

    @Test
    public void test() {
        User user1 = SpringContextUtil.getBean("user");

        user1.setId("3526");
        user1.setName("Mr Hu");
        user1.setPassword("12345");
        user1.setSex("0");
        user1.setPhone("1546178468");
        user1.setEmail("18964871648@qq.com");
        user1.setRole(0);

        Item item = SpringContextUtil.getBean("item");
        item.setId("");
        item.setName("iu");
        item.setCount(1);
        item.setTid("3526");

        EvidentFile file = SpringContextUtil.getBean("file");
        file.setName("fisuhefi");
        file.setPath("sges");
        file.setId(1);
        file.setTid("3526");
        file.setItemId("");

        ItemDAO itemDAO = SpringContextUtil.getBean(ItemDAO.class);
        itemDAO.addItem(item);

        EvidentFileDAO fileDAO = SpringContextUtil.getBean(EvidentFileDAO.class);
        fileDAO.addFile(file);

        UserDAO userDAO = SpringContextUtil.getBean(UserDAO.class);
        int num = userDAO.addUser(user1);

        System.out.println("Num: "+num);
    }

    @Test
    public void test2() {

        UserService userService = SpringContextUtil.getBean(UserService.class);
        User user = userService.queryUserById("53280");
        System.out.println("flag");
    }
}
