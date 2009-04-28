package net.sourceforge.vrapper.vim.commands;

import net.sourceforge.vrapper.keymap.KeyStroke;
import net.sourceforge.vrapper.platform.TextContent;
import net.sourceforge.vrapper.utils.Function;
import net.sourceforge.vrapper.utils.LineInformation;
import net.sourceforge.vrapper.utils.Position;
import net.sourceforge.vrapper.vim.EditorAdaptor;

/**
 * Replaces the character at the current position with another one.
 *
 * @author Matthias Radig
 */
public class ReplaceCommand extends CountAwareCommand {

    public static final Function<Command, KeyStroke> KEYSTROKE_CONVERTER = new Function<Command, KeyStroke>() {
        public Command call(KeyStroke arg) {
            return new ReplaceCommand(arg.getCharacter());
        }
    };

    private final char replacement;

    public ReplaceCommand(char replacement) {
        super();
        this.replacement = replacement;
    }

    @Override
    public void execute(EditorAdaptor editorAdaptor, int count) {
        if (count == NO_COUNT_GIVEN) {
            count = 1;
        }
        Position position = editorAdaptor.getPosition();
        TextContent c = editorAdaptor.getModelContent();
        LineInformation line = c.getLineInformationOfOffset(position.getModelOffset());
        Position targetOffset = position.addModelOffset(count - 1);
        if (targetOffset.getModelOffset() < line.getEndOffset()) {
            StringBuilder s = new StringBuilder();
            for(int i = 0; i < count; i++) {
                s.append(replacement);
            }
            c.replace(position.getModelOffset(), s.length(), s.toString());
            editorAdaptor.setPosition(targetOffset, true);
        }
    }


    @Override
    public CountAwareCommand repetition() {
        return this;
    }

}