package edu.moravian;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.exceptions.StorageException;
import java.awt.*;

public class DiscordBot
{
    public static void main(String[] args)
    {
        DatabaseManager storage = createStorage();
        BotResponder responder = createResponder(storage);
        String token = loadToken();
        startBot(responder, token);
    }

    private static RedisDatabase createStorage()
    {
        RedisDatabase storage = null;
        try
        {
            storage = new RedisDatabase("localhost", 6379);
            storage.testConnection();
        }
        catch (StorageException e)
        {
            System.err.println("Failed to connect to Redis\n\nIs it running?");
            System.exit(1);
        }

        return storage;
    }

    private static BotResponder createResponder(DatabaseManager storage)
    {
        TriviaGame game = new TriviaGame(storage);
        return new BotResponder(game);
    }

    private static String loadToken()
    {
        try
        {
            Dotenv dotenv = Dotenv.load();
            return dotenv.get("DISCORD_TOKEN");
        }
        catch(DotenvException e)
        {
            System.err.println("Failed to load .env file\n\nIs it present?");
            System.exit(1);
            return null;
        }
    }

    private static void startBot(BotResponder responder, String token)
    {
        JDA api = JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();

        api.addEventListener(new ListenerAdapter()
        {
            @Override
            public void onMessageReceived(MessageReceivedEvent event)
            {
                if (event.getAuthor().isBot())
                    return;

                if (!event.getChannel().getName().equals("channel-name"))
                    return;

                String username = event.getAuthor().getName();
                String message = event.getMessage().getContentRaw();

                int color = message.hashCode() % 2 == 0 ? 0x552583 : 0xFDB927;
                String response = responder.respond(username, message);
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("🏀🏀🏀🏀🏀🏀🏀🏀")
                        .setDescription(response)
                        .setColor(color);
                event.getChannel().sendMessageEmbeds(embed.build()).queue();
            }
        });
    }
}
