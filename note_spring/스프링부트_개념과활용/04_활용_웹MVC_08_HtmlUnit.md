# 스프링 웹 MVC 8부: HtmlUnit

HTML 템플릿 뷰 테스트를 보다 전문적으로 하자.

* http://htmlunit.sourceforge.net/

* http://htmlunit.sourceforge.net/gettingStarted.html

* 의존성 추가

  ```xml
  <dependency>
     <groupId>org.seleniumhq.selenium</groupId>
     <artifactId>htmlunit-driver</artifactId>
     <scope>test</scope>
  </dependency>
  <dependency>
     <groupId>net.sourceforge.htmlunit</groupId>
     <artifactId>htmlunit</artifactId>
     <scope>test</scope>
  </dependency>
  ```

* @Autowire WebClient 



---

# HtmlUnit

* html을 단위테스트하기 위한 툴

* Get started

  WebClient만들어서 요청을 보내고, 결과를 HtmlPage 인터페이스로 받아와서 안에 있는 내용을 가져올 수 있다. 

  ```java
  @Test
  public void homePage() throws Exception {
      try (final WebClient webClient = new WebClient()) {
          final HtmlPage page = webClient.getPage("http://htmlunit.sourceforge.net");
          Assert.assertEquals("HtmlUnit - Welcome to HtmlUnit", page.getTitleText());
  
          final String pageAsXml = page.asXml(); //xml로 응답을 가져옴
          Assert.assertTrue(pageAsXml.contains("<body class=\"composite\">"));
  
          final String pageAsText = page.asText(); //text로 응답을 가져옴
          Assert.assertTrue(pageAsText.contains("Support for the HTTP and HTTPS protocols"));
      }
  }
  ```

  

테스트 코드 작성

* WebClient를 주입받아서 사용 (※ MockMvc도 같이 사용 가능)

```java
@RunWith(SpringRunner.class)
@WebMvcTest(SampleController.class)
public class SampleControllerTest {

    @Autowired
    WebClient webClient;

    @Test
    public void hello() throws Exception {
        // 요청 "/hello"
        // 응답
        // - 모델 name : solar
        // - 뷰 name : hello
        // - 본문 내용 : solar

        HtmlPage page = webClient.getPage("/hello");
        HtmlHeading1 h1 = page.getFirstByXPath("//h1");
        assertThat(h1.getTextContent()).isEqualToIgnoringCase("solar");
    }
}
```







---

## 핵심 기술

* [Docs](http://htmlunit.sourceforge.net/gettingStarted.html)

## Submitting a form

form을 출력하는 화면을 가져와서 form에 submit하는 use case를 목각할 수 ㅇ있다.

```java
@Test
public void submittingForm() throws Exception {
    try (final WebClient webClient = new WebClient()) {

        // Get the first page
        final HtmlPage page1 = webClient.getPage("http://some_url");

        // Get the form that we are dealing with and within that form, 
        // find the submit button and the field that we want to change.
        final HtmlForm form = page1.getFormByName("myform");

        final HtmlSubmitInput button = form.getInputByName("submitbutton");
        final HtmlTextInput textField = form.getInputByName("userid");

        // Change the value of the text field
        textField.type("root");

        // Now submit the form by clicking the button and get back the second page.
        final HtmlPage page2 = button.click();
    }
}
```



## Imitating a specific browser

특정 브라우저 테스트 진행

```java
@Test
public void homePage_Firefox() throws Exception {
    try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52)) {
        final HtmlPage page = webClient.getPage("http://htmlunit.sourceforge.net");
        Assert.assertEquals("HtmlUnit - Welcome to HtmlUnit", page.getTitleText());
    }
}
```



## Finding a specific element

HTML 페이지에 대한 참조가 있으면 다음 중 하나를 사용하여 특정 HTML 요소를 검색할 수 있습니다. 'get' 메서드 또는 XPath 또는 CSS 선택기를 사용합니다.

### Traversing the DOM tree

Below is an example of finding a 'div' by an ID, and getting an anchor by name:

html 문서의 element를 가져와서 값을 비교&확인할 수 있다.

```java
@Test
public void getElements() throws Exception {
    try (final WebClient webClient = new WebClient()) {
        final HtmlPage page = webClient.getPage("http://some_url");
        final HtmlDivision div = page.getHtmlElementById("some_div_id");
        final HtmlAnchor anchor = page.getAnchorByName("anchor_name");
    }
}
```

A simple way for finding elements might be to find all elements of a specific type.

```java
 @Test
 public void getElements() throws Exception {
     try (final WebClient webClient = new WebClient()) {
         final HtmlPage page = webClient.getPage("http://some_url");
         NodeList inputs = page.getElementsByTagName("input");
         final Iterator<E> nodesIterator = nodes.iterator();
         // now iterate
     }
 }
```

