package micro.service.demo.clients;

import micro.service.demo.config.Feignconfigs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "demo2", configuration = Feignconfigs.class)
public interface IDemo2Client {
    @RequestMapping(value = "/user/introduce", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    Object getUserIntroduce();
}
