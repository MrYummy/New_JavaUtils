
command signs {

    claim {
        [string:owner] {
            perm utils.serversigns.claims.other;
            run signs_set_custom owner;
            help Claims the sign so that you can add messages to it;
        }
        perm utils.serversigns.claims.other;
        run signs_set;
        help Claims the sign so that you can add messages to it;
    }

    info {
        perm utils.serversigns.use;
        help Displays information about the claimed sign;
        run signs_info;
    }

    add [string:message][optional:++] {
        perm utils.serversigns.use;
        help Adds the message the sign. Use ++ at the end\n to add the message to your buffer. You can then use\n the same command again to create a longer message;
        run signs_add message ++;
    }

    remove [int:id] {
        perm utils.serversigns.use;
        help Removes the message with the given ID from the sign.\n The ID is given before each message by &b/svs info&a.;
        run signs_remove id;
    }

    switch [int:id1] [int:id2] {
        perm utils.serversigns.use;
        help Reverses the order in which the given messages are shown.;
        run signs_switch id1 id2;
    }

    clear {
        perm utils.serversigns.use;
        help Removes all messages from the sign;
        run signs_clear;
    }

    unclaim {
        perm utils.serversigns.use;
        help Resets the sign, removing all messages and its owner;
        run signs_unclaim;
    }

    commands {
        perm utils.serversigns.use;
        help Displays a list of whitelisted commands;
        run signs_commands;
    }

    help {
        [int:page] {
            perm utils.serversigns.use;
            help Displays this help page;
            run signs_help page;
        }
        perm utils.serversigns.use;
        help Displays this help page;
        run signs_help 1;
    }

}