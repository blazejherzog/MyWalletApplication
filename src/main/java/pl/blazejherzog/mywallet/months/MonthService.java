package pl.blazejherzog.mywallet.months;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MonthService {

    @Autowired
    MonthRepository monthRepository;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/months")
    public ResponseEntity addMonth(@RequestBody Month month) throws JsonProcessingException {
        Optional<Month> monthFromDb = monthRepository.findById(month.getId());
        if (monthFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        Month savedMonth = monthRepository.save(month);
        return ResponseEntity.ok(objectMapper.writeValueAsString(savedMonth));
    }

    @GetMapping("/months/all")
    public ResponseEntity getAllMonths() throws JsonProcessingException {
        List<Month> allMonths = monthRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(allMonths));
    }

    @GetMapping("/months/{id}")
    public ResponseEntity getMonthById(@PathVariable String monthId) throws JsonProcessingException {
        Optional<Month> monthFromDb = monthRepository.findAll().stream()
                .filter(month -> month.getId().equals(monthId))
                .findFirst();
        if (monthFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(objectMapper.writeValueAsString(monthFromDb));
    }

    @GetMapping("/months/year/{year}")
    public ResponseEntity getMonthsByYear(@PathVariable int year) throws JsonProcessingException {
        List<Month> monthsByYear = monthRepository.findAll().stream()
                .filter(month -> month.getYear() == year)
                .collect(Collectors.toList());
        return ResponseEntity.ok(objectMapper.writeValueAsString(monthsByYear));
    }

    @DeleteMapping("/months/{id}")
    public void deleteMonthById(@PathVariable String id) {
        List<Month> months = monthRepository.findAll();
        for (Month month : months) {
            if (month.getId().equals(id)) {
                monthRepository.delete(month);
            }
        }
    }
}
