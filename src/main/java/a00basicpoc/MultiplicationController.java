package a00basicpoc;

// MultiplicationController.java

import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*") //2nd step
@RestController  //1st step
@RequestMapping("/api/multiply")  // nice to have..
public class MultiplicationController {

    // registering for get event...
    @GetMapping("/fifth")
    public int getFifthMultiple(@RequestParam int number) {
        System.out.println("freak freak freak");
        return  number *5;
    }

    // registering for post event
    @PostMapping("/eighth")
    public int getEighthMultiple(@RequestBody SeriousStuff request) {

        System.out.println("junk junk junk");
        return request.getFreak() * 8;
    }

    // DTO for the POST body
    public static class SeriousStuff {
        private int freak;

        public int getFreak() {
            return freak;
        }

        public void setFreak(int freak) {
            this.freak = freak;
        }
    }
}