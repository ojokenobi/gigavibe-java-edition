package Bots.commands;

import Bots.BaseCommand;
import Bots.MessageEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Objects;

import static Bots.Main.*;
import static Bots.Main.createQuickError;

public class CommandGetDump extends BaseCommand {
    @Override
    public void execute(MessageEvent event) {
        if (event.getUser().getIdLong() == 211789389401948160L || event.getUser().getIdLong() == 260016427900076033L) {
            if (new File("log.txt").exists()) {
                new File("log.txt").delete();
            }
            String PID = "";
            try {
                Process p = Runtime.getRuntime().exec("jps");
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] jpsOutputLine = line.split(" ");
                    if (jpsOutputLine[1].equals("bot") || jpsOutputLine[1].equals("bot.jar")) {
                        PID = jpsOutputLine[0];
                    }
                }
                reader.close();
                if (Objects.equals(PID, "")) {
                    event.replyEmbeds(createQuickError("Could not get dump as the process ID was not found."));
                    return;
                }
                Runtime.getRuntime().exec("jstack " + PID + " > log.txt");
            } catch (Exception e) {
                e.printStackTrace();
                event.replyEmbeds(createQuickError("Could not get dump.\n```\n" + e.getMessage() + "\n```"));
            }
            event.replyFiles(FileUpload.fromData(new File("log.txt")));
            try {
                Thread.sleep(10000);
            } catch (Exception ignored){}
            new File("log.txt").delete();
        } else {
            event.replyEmbeds(createQuickError("You do not have the permissions for this."));
        }
    }

    @Override
    public String getCategory() {
        return "Dev";
    }

    @Override
    public String getOptions() {
        return "";
    }

    @Override
    public String[] getNames() {
        return new String[]{"killytdlp", "killall", "killdl"};
    }

    @Override
    public String getDescription() {
        return "Kills YT-DLP processes, should only be used if a download is hanging.";
    }

    @Override
    public long getRatelimit() {
        return 0;
    }
}