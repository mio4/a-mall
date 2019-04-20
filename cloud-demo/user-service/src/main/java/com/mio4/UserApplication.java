package  com.mio4;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mio4.user.mapper")
public class UserApplication {

}
