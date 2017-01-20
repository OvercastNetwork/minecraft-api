package tc.oc.minecraft.api.text;

import net.md_5.bungee.api.chat.BaseComponent;
import tc.oc.minecraft.api.command.CommandSender;
import tc.oc.minecraft.api.command.Console;
import tc.oc.minecraft.api.entity.Player;

/**
 * A plugin can register one of these with a {@link TextRendererBinder} in order to process
 * {@link BaseComponent}s before they are sent to individual {@link CommandSender}s.
 *
 * The server will pass most text through all registered {@link TextRenderer}s, in unspecified order.
 * Rendering happens on the main thread, just before the text is displayed or transmitted.
 *
 * Implementors must NOT modify the text passed to the render method. The original text can be
 * returned unmodified, or a new object can be returned. Returned objects are not modified
 * by the server, so they can be reused for other renderings.
 *
 * Implementors should try to be fast and minimize object creation, as rendering is called once
 * per player, per piece of text displayed, even for broadcasted messages.
 *
 * @see TextRenderContext
 * @see TextRendererBinder
 */
public interface TextRenderer {

    default BaseComponent render(TextRenderContext context, BaseComponent text, CommandSender viewer) {
        if(viewer instanceof Player) {
            return render(context, text, (Player) viewer);
        } else if(viewer instanceof Console) {
            return render(context, text, (Console) viewer);
        } else {
            return text;
        }
    }

    default BaseComponent render(TextRenderContext context, BaseComponent text, Player viewer) {
        return text;
    }

    default BaseComponent render(TextRenderContext context, BaseComponent text, Console viewer) {
        return text;
    }
}
