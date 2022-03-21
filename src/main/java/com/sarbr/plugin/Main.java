package com.sarbr.plugin;

import com.sarbr.plugin.exception.PluginException;
import com.sarbr.plugin.support.PluginSplashScreen;
import com.sarbr.plugin.utils.AlertHelper;
import com.sarbr.plugin.utils.DateUtils;
import com.sarbr.plugin.utils.SpringContextUtil;
import com.sarbr.plugin.view.AppView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

@SpringBootApplication
public class Main extends AbstractJavaFxApplicationSupport {

    private final Logger log = LoggerFactory.getLogger(Main.class);
    private final static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public static void main(String[] args) {
        launch(Main.class, AppView.class, new PluginSplashScreen(), args);
    }

    @Override
    public void init() {
        log.info("init");
        String userText = System.getProperty("user.name") + "，" + DateUtils.hello();
        System.setProperty("JavaFX.user", userText);
        Platform.runLater(()->{
            try {
                super.init();
            } catch (Exception e) {
                Platform.exit();
            }
        });
    }

    @Override
    public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
        log.info("beforeInitialView");
        super.beforeInitialView(stage, ctx);
        SpringContextUtil.setApplicationContext(ctx);
    }

    // 虽然在application.yml中可以设置应用图标，但是首屏启动时的应用图标未改变，建议在此处覆盖默认图标
    @Override
    public Collection<Image> loadDefaultIcons() {
        return Collections.singletonList(new Image(getURL("icon/icon.png").toExternalForm()));
    }

    /**
     * resources文件夹下
     */
    public static URL getURL(String file) {
        final Resource resource = resolver.getResource(file);
        try {
            return resource.getURL();
        } catch (IOException e) {
            AlertHelper.errAlter("未找到资源!");
            throw new PluginException(e.getMessage());
        }
    }

    public static Parent loadFXML(String fxml) {
        try {
            return new FXMLLoader(getURL(fxml)).load();
        } catch (IOException e) {
            AlertHelper.errAlter(e.getMessage());
        }
        return new Pane();
    }

}