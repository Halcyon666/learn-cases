package whalefall.lambda;

import com.whalefall.learncases.valid.standvalid.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Slf4j
class CollectorsTest {

    private static void getAccount(String mail, List<UserAccount> accounts) {
        UserAccount userAccount = new UserAccount();
        userAccount.setName("1");
        userAccount.setEmail(mail);
        accounts.add(userAccount);
    }

    private static Function<HashMap<String, String>, String> getFieldFunction(String email) {
        return s -> s.get(email);
    }

    private static Predicate<HashMap<String, String>> getNotBlankFieldPredicate(String field) {
        return s -> StringUtils.isNotBlank(s.get(field));
    }

    private static List<HashMap<String, String>> getHashMapList() {
        List<HashMap<String, String>> list = new ArrayList<>();
        putMap2List("a", "a0@qq.com", list);
        putMap2List("a", "a1@qq.com", list);
        putMap2List("a", "a2@qq.com", list);
        putMap2List("a", "a1@qq.com", list);
        return list;
    }

    private static void printMap(Map<String, Set<String>> emailsByName) {
        emailsByName.forEach((name, emailSet) -> emailSet.forEach(email -> log.info("name = " + name + ", email = " + email)));
    }

    private static void putMap2List(String name, String mail, List<HashMap<String, String>> list) {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("email", mail);
        list.add(map);
    }

    /**
     * {@link Collectors#mapping(Function, Collector)}
     */
    @Test
    void testBeanGroupBy() {
        List<UserAccount> accounts = new ArrayList<>();

        getAccount("1234@qq.com", accounts);

        getAccount("1234@gmail.com", accounts);

        Map<String, Set<String>> emailsByName = accounts.stream().collect(groupingBy(UserAccount::getName, mapping(UserAccount::getEmail, toSet())));
        printMap(emailsByName);
        Assertions.assertEquals(1, emailsByName.size(), "emails lengths is not correct");
    }

    @Test
    void tesHashMapGroupBy() {
        List<HashMap<String, String>> list = getHashMapList();
        Map<String, Set<String>> emailsByName = list.stream().collect(groupingBy(getFieldFunction("name"), mapping(getFieldFunction("email"), toSet())));
        printMap(emailsByName);
        Assertions.assertEquals(1, emailsByName.size(), "emails lengths is not correct");
    }

    @Test
    void testNullKeyNullValueHashMapGroupBy() {
        List<HashMap<String, String>> list = getHashMapList();
        putMap2List(null, "a1@qq.com", list);
        putMap2List(null, null, list);
        putMap2List("a", null, list);
        Map<String, Set<String>> emailsByName1 = list.stream()
                .filter(getNotBlankFieldPredicate("name"))
                .filter(getNotBlankFieldPredicate("email"))
                .collect(groupingBy(getFieldFunction("name"), mapping(getFieldFunction("email"), toSet())));
        printMap(emailsByName1);
        Assertions.assertEquals(1, emailsByName1.size(), "emails lengths is not correct");
    }

    @Test
    void testNullKeyNullValueHashMapPartitionBy() {
        List<HashMap<String, String>> list = getHashMapList();
        putMap2List(null, "a1@qq.com", list);
        putMap2List(null, null, list);
        putMap2List("a", null, list);
        Map<Boolean, Set<String>> collect = list.stream()
                .collect(partitioningBy(getNotBlankFieldPredicate("name"), mapping(getFieldFunction("email"), toSet())));

        collect.forEach((b, emailSet) -> emailSet.forEach(email -> log.info("b = " + b + ", email = " + email)));
        Assertions.assertEquals(2, collect.size(), "emails lengths is not correct");
    }

    /**
     * <a href="https://vimsky.com/examples/usage/pattern-splitasstream-method-in-java-with-examples.html">代码来自</a>
     */
    @Test
    void testSplitAsStream() {
        // create a regex String
        String regex = "geeks";

        // create the string
        // in which you want to search
        String actualString = "Welcome to geeks for geeks";

        // create a Pattern using regex
        Pattern pattern = Pattern.compile(regex);

        // split the text
        Stream<String> stream = pattern.splitAsStream(actualString);

        // print array
        stream.forEach(System.out::println);

        Assertions.assertNotNull(stream);
    }
}
