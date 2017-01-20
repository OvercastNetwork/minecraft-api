package tc.oc.minecraft.api.text;

import net.md_5.bungee.api.chat.BaseComponent;
import tc.oc.minecraft.api.command.CommandSender;

/**
 * A service that "renders" {@link BaseComponent}s for display to a specific {@link CommandSender}.
 *
 * This may involve translation of {@link net.md_5.bungee.api.chat.TranslatableComponent}s,
 * or other operations implemented by plugins. It also may do nothing at all, in which case
 * the {@link #render(BaseComponent, CommandSender)} method is likely to return the same
 * {@link BaseComponent} that was passed to it.
 *
 * @see TextRenderer
 */
public interface TextRenderContext {

    BaseComponent render(BaseComponent text, CommandSender viewer);
}
