package PluginRunner;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import edu.udel.RunHostsKt;

public class RunHosts_main extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        for (Project project : ProjectManager.getInstance().getOpenProjects()) {

            Project[] args = new Project[]{project};
            RunHostsKt.main(args);
        }

        System.exit(0);
    }
}
