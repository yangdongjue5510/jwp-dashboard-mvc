package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ForwardController;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class AnnotationHandlerAdapterTest {

    private final AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();

    @Test
    @DisplayName("어댑터가 HandlerExecution을 구현한 핸들러는 지원한다.")
    void supports() {
        // given
        final HandlerExecution supportedHandler = new HandlerExecution(null, null);
        final Controller notSupportedHandler = new ForwardController("");

        // when
        final boolean notSupportExpected = adapter.supports(notSupportedHandler);
        final boolean supportExpected = adapter.supports(supportedHandler);

        // then
        assertAll(
                () -> assertThat(notSupportExpected).isFalse(),
                () -> assertThat(supportExpected).isTrue()
        );
    }

    @Test
    @DisplayName("어댑터가 핸들러를 수행시킨다.")
    void handle() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        given(request.getAttribute("id")).willReturn("gugu");
        final Method method = TestController.class
                .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final HandlerExecution handlerExecution = new HandlerExecution(method, new TestController());

        // when
        final ModelAndView expect = adapter.handle(request, response, handlerExecution);

        // then
        assertThat(expect).isNotNull();
    }
}