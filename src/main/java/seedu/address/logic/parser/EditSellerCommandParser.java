package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HOUSE_INFO;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SELLING_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandWarnings;
import seedu.address.logic.commands.EditSellerCommand;
import seedu.address.logic.commands.EditSellerCommand.EditSellerDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditSellerCommand object
 */
public class EditSellerCommandParser implements Parser<EditSellerCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditSellerCommand
     * and returns an EditSellerCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditSellerCommand parse(String args, CommandWarnings commandWarnings) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_SELLING_ADDRESS, PREFIX_HOUSE_INFO, PREFIX_TAG, PREFIX_PRIORITY);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditSellerCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_SELLING_ADDRESS, PREFIX_HOUSE_INFO, PREFIX_PRIORITY);

        EditSellerDescriptor editSellerDescriptor = new EditSellerDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editSellerDescriptor.setName(ParserUtil.parseName(commandWarnings,
                    argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editSellerDescriptor.setPhone(ParserUtil.parsePhone(commandWarnings,
                    argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editSellerDescriptor.setEmail(ParserUtil.parseEmail(commandWarnings,
                    argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editSellerDescriptor.setAddress(ParserUtil.parseAddress(commandWarnings,
                    argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        if (argMultimap.getValue(PREFIX_SELLING_ADDRESS).isPresent()) {
            editSellerDescriptor.setSellingAddress(ParserUtil.parseAddress(commandWarnings,
                    argMultimap.getValue(PREFIX_SELLING_ADDRESS).get()));
        }
        if (argMultimap.getValue(PREFIX_HOUSE_INFO).isPresent()) {
            editSellerDescriptor.setHouseInfo(ParserUtil.parseHouseInfo(commandWarnings,
                    argMultimap.getValue(PREFIX_HOUSE_INFO).get()));
        }
        parseTagsForEdit(commandWarnings, argMultimap.getAllValues(PREFIX_TAG))
                .ifPresent(editSellerDescriptor::setTags);
        if (argMultimap.getValue(PREFIX_PRIORITY).isPresent()) {
            editSellerDescriptor.setPriority(ParserUtil.parsePriority(commandWarnings,
                    argMultimap.getValue(PREFIX_PRIORITY).get()));
        }

        if (!editSellerDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditSellerCommand.MESSAGE_NOT_EDITED);
        }

        return new EditSellerCommand(index, editSellerDescriptor, commandWarnings);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(CommandWarnings commandWarnings, Collection<String> tags)
            throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(commandWarnings, tagSet));
    }

}
