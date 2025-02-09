package org.flatscrew.latte.springexample;

import org.flatscrew.latte.Program;
import org.flatscrew.latte.springexample.view.MainViewModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LatteCommandLineRunner implements CommandLineRunner {

    private final MainViewModel latteApplication;

    public LatteCommandLineRunner(MainViewModel latteApplication) {
        this.latteApplication = latteApplication;
    }

    @Override
    public void run(String... args) throws Exception {
        new Program(latteApplication)
                .withAltScreen()
                .run();
    }
}
