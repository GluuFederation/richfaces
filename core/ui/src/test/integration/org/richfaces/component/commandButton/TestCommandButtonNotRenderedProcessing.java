package org.richfaces.component.commandButton;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.faces.context.PartialViewContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.CoreUIDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import com.google.common.base.Predicate;

/**
 * Tests a4j:commandButton processing using {@link PartialViewContext}. (RF-12145)
 */
@RunAsClient
@RunWith(Arquillian.class)
public class TestCommandButtonNotRenderedProcessing {

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @FindBy(id = "button")
    WebElement button;

    @FindBy(id = "output")
    WebElement output;

    @Deployment
    public static WebArchive createDeployment() {
        CoreUIDeployment deployment = new CoreUIDeployment(TestCommandButtonNotRenderedProcessing.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void when_executed_component_is_not_rendered_after_ajax_request_then_its_oncomplete_handler_should_be_executed() {
        browser.get(contextPath.toExternalForm());

        guardXhr(button).click();

        waitAjax().withTimeout(1, TimeUnit.SECONDS).until(new Predicate<WebDriver>() {

            @Override
            public boolean apply(WebDriver input) {
                return (Boolean) ((JavascriptExecutor) browser).executeScript("return !!window.oncompleteEvaluated");
            }
        });
    }

    @Test
    public void when_executed_component_is_not_rendered_after_ajax_request_then_its_render_target_should_be_taken_into_consideration() {
        browser.get(contextPath.toExternalForm());

        guardXhr(button).click();

        waitAjax().until().element(output).text().equalTo("postback");

        try {
            // button should not be present
            button.click();
            fail();
        } catch (NoSuchElementException e) {
        }
    }

    private static void addIndexPage(CoreUIDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("a4j", "http://richfaces.org/a4j");

        p.form("<a4j:outputPanel id='panel'>");
        p.form("    <a4j:commandButton id='button' render='panel output' oncomplete='window.oncompleteEvaluated = true' rendered='#{!facesContext.postback}' />");
        p.form("</a4j:outputPanel>");

        p.form("<a4j:outputPanel id='output'>#{facesContext.postback ? 'postback' : 'initial'}</a4j:outputPanel>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
