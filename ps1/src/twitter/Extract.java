/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.Set;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract consists of methods that extract information from a list of tweets.
 *
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     *
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets.isEmpty()) {
            return new Timespan(Instant.EPOCH, Instant.EPOCH);
        }

        Instant start = tweets.get(0).getTimestamp();
        Instant end = start;

        for (Tweet tweet : tweets) {
            Instant time = tweet.getTimestamp();

            if (time.isBefore(start)) {
                start = time;
            }

            if (time.isAfter(end)) {
                end = time;
            }
        }

        return new Timespan(start, end);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     *
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames mentioned in the text of the tweets.
     *         Usernames are case-insensitive, and each username appears at
     *         most once in the returned set.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentionedUsers = new HashSet<>();

        for (Tweet tweet : tweets) {
            Matcher matcher = MENTION.matcher(tweet.getText());

            while (matcher.find()) {
                String username = matcher.group(1).toLowerCase(Locale.ROOT);
                mentionedUsers.add(username);
            }
        }

        return mentionedUsers;
    }

    /*
     * A mention is @ followed by a username. The @ cannot be immediately
     * preceded or followed by a character valid in a Twitter username.
     */
    private static final Pattern MENTION =
            Pattern.compile("(?<![A-Za-z0-9_])@([A-Za-z0-9_]+)(?![A-Za-z0-9_])");
}