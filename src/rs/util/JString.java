package rs.util;

import rs.Game;
import rs.io.Buffer;

public class JString {

    /* @formatter:off */
	public static final String[] COMMON_OBJ_ACTIONS = {
		"Wear", "Rub", "Wield", "Destroy", "Open", "Empty",
		"Drink", "Remove one", "Remove-one", "Move", "Read", "Eat",
		"Fill", "Identify"
	};
	
	public static final String[] COMMON_LOC_ACTIONS = {
		"Cross", "Jump", "Walk-up", "Repair", "Hack", "Chop down",
		"Pull", "Climb-down", "Chop-down", "Open", "Climb-up", "Pick",
		"Mine", "Prospect", "Read", "Look", "Search", "Enter",
		"Use", "Close", "Inspect", "Guide", "Climb"
	};
	/* @formatter:on */

    public static final String FPS_ = "Fps: ";
    public static final String MEM_ = "Mem: ";
    public static final String LOCAL_ = "Local: ";
    public static final String TILE_ = "Tile: ";
    public static final String SCENE_ = "Scene: ";
    public static final String CAMERA_ = "Camera: ";
    public static final String IN_ = "In: ";
    public static final String OUT_ = "Out: ";
    public static final String BS_ = "B/s";
    public static final String OK = "Ok";
    public static final String SELECT = "Select";
    public static final String CONTINUE = "Continue";
    public static final String _BILLION_ = " billion ";
    public static final String _MILLION_ = " million ";
    public static final String ACCOUNT_DISABLED = "Your account has been disabled.";
    public static final String ACCOUNT_MANAGEMENT = "Do this from the 'account management' area on our front webpage";
    public static final String ALREADY_LOGGED_IN = "Your account is already logged in.";
    public static final String ATTACK = "attack";
    public static final String ATTEMPTING_TO_REESTABLISH = "Please wait - attempting to reestablish";
    public static final String BAD_SESSION = "Bad session id.";
    public static final String BLANK = "";
    public static final String BOUNDS_ERROR = "Bounds error";
    public static final Buffer buffer = new Buffer(new byte[100]);
    public static final String CANCEL_RECOVERIES = "If you do not remember making this change then cancel it immediately";
    public static final char[] FORMAT_BUFFER = new char[100];
    public static final String CHECK_MESSAGES = "Please check your message-centre for details.";
    public static final String CHOOSE_OPTION = "Choose Option";
    public static final String CONNECTING_TO_FRIENDSERVER = "Connecting to friendserver";
    public static final String CONNECTING_TO_SERVER = "Connecting to server...";
    public static final String CONNECTING_TO_UPDATE_SERVER = "Connecting to update server";
    public static final String CONNECTION_ERROR = "Connection error";
    public static final String CONNECTION_LOST = "Connection lost";
    public static final String COULDNT_LOGIN = "Could not complete login.";
    public static final String CROSS = "cross";
    public static final String CYA = "@cya@";
    public static final String DAYS_AGO = " days ago";
    public static final String DIFFERENT_WORLD = "Please use a different world.";
    public static final String EARLIER_TODAY = "earlier today";
    public static final String EEK = "eek";
    public static final String ENTER_YOUR_CREDENTIALS = "Enter your username & password.";
    public static final String EOF_ERROR = "EOF error";
    public static final String ERROR_CONNECTING = "Error connecting to server.";
    public static final String FRIENDS = "Friends";
    public static final String FRIENDS_FULL = "Your friendlist is full. Max of 100 for free users, and 200 for members";
    public static final String GR2 = "@gr2@";
    public static final String GRE = "@gre@";
    public static final String HEADICONS = "headicons";
    public static final String HELVETICA = "Helvetica";
    public static final String HIDE = "Hide";
    public static final String HITMARKS = "hitmarks";
    public static final String INVALID_CREDENTIALS = "Invalid username or password.";
    public static final String INVALID_SERVER = "Invalid loginserver requested";
    public static final String INVALID_STRING = "invalid_string";
    public static final String JUST_LEFT_WORLD = "You have only just left another world";
    public static final String K_ = "K ";
    public static final String HIDDEN = "hidden";
    public static final String MEMBERS_OBJECT = "Members Object";
    public static final String LOGIN_TO_A_MEMBERS_SERVER_TO_USE_THIS_OBJECT = "Login to a members' server to use this object.";
    public static final String LOADING = "Loading...";
    public static final String LOADING_FRIEND_LIST = "Loading friend list";
    public static final String LOADING_IGNORE_LIST = "Loading ignore list";
    public static final String LOADING_PLEASE_WAIT = "RuneScape is loading - please wait...";
    public static final String LOGIN_EXCEEDED = "Login attempts exceeded.";
    public static final String LOGIN_LIMIT = "Login limit exceeded";
    public static final String MAP_DOTS = "mapdots";
    public static final String MAP_FUNCTION = "mapfunction";
    public static final String MAP_MARKER = "mapmarker";
    public static final String MAP_SCENE = "mapscene";
    public static final String MOD_ICONS = "mod_icons";
    public static final String MOVE_TO_FREE = "To play on this world move to a free area first";
    public static final String MUTE_OFF = "Moderator option: Mute player for 48 hours: <OFF>";
    public static final String MUTE_ON = "Moderator option: Mute player for 48 hours: <ON>";
    public static final String NEED_MEMBERS = "You need a members account to login to this world.";
    public static final String NO_RECOVERIES = "You have not yet set any password recovery questions.";
    public static final String NO_RESPONSE = "No response from loginserver";
    public static final String NON_MEMBERS_1 = "@yel@This is a non-members world: @whi@Since you are a member we";
    public static final String NON_MEMBERS_2 = "@whi@recommend you use a members world instead. You may use";
    public static final String NON_MEMBERS_3 = "@whi@this world but member benefits are unavailable whilst here.";
    public static final String NULL_ERROR = "Null error";
    public static final String OFF = "Off";
    public static final String OFFLINE = "@red@Offline";
    public static final String ON = "On";
    public static final String PLEASE_SUBSCRIBE = "Please subscribe, or use a different world.";
    public static final String PLEASE_WAIT = "Please wait...";
    public static final String PREPARING_GAME_ENGINE = "Preparing game engine";
    public static final String PRIVATE_CHAT = "Private chat";
    public static final String PUBLIC_CHAT = "Public chat";
    public static final String REDSTONE_1 = "redstone1";
    public static final String REDSTONE_2 = "redstone2";
    public static final String REDSTONE_3 = "redstone3";
    public static final String REJECTED_SESSION = "Login server rejected session.";
    public static final String RELOAD_PAGE = "Please reload this page.";
    public static final String REPORT_ABUSE = "Report abuse";
    public static final String REQUESTING_ = "Requesting ";
    public static final String REQUESTING_ANIMATIONS = "Requesting animations";
    public static final String REQUESTING_MAPS = "Requesting maps";
    public static final String REQUESTING_MODELS = "Requesting models";
    public static final String SCROLLBAR = "scrollbar";
    public static final String SECURE_YOUR_ACCOUNT = "We strongly recommend you do so now to secure your account.";
    public static final String SERVER = "127.0.0.1";
    public static final String SERVER_OFFLINE = "Login server offline.";
    public static final String SERVER_UPDATING = "The server is being updated.";
    public static final String SIDE_ICONS = "sideicons";
    public static final char SPACE = ' ';
    public static final String STARTING_UP = "Starting up";
    public static final String SYSTEM_UPDATE_IN = "System update in: ";
    public static final String TOO_MANY_CONNECTIONS = "Too many connections from your address.";
    public static final String TRADE_COMPETE = "Trade/compete";
    public static final String TRANSFERRED = "Your profile will be transferred in: ";
    public static final String TRY_AGAIN = "Please try again.";
    public static final String TRY_AGAIN_IN_60 = "Try again in 60 secs...";
    public static final String UNABLE_TO_CONNECT = "Unable to connect.";
    public static final String UNEXPECTED_ERROR = "Unexpected error";
    public static final String UNEXPECTED_RESPONSE = "Unexpected server response";
    public static final String UNKNOWN_ERROR = "Unknown error";
    public static final String UNPACKING_CONFIG = "Unpacking config";
    public static final String UNPACKING_MEDIA = "Unpacking media";
    public static final String UNPACKING_SOUNDS = "Unpacking sounds";
    public static final String UNPACKING_TEXTURES = "Unpacking textures";
    public static final String UNPACKING_WIDGETS = "Unpacking widgets";
    public static final String UNREAD_MESSAGE = "unread message";
    public static final String UNREAD_MESSAGE_1 = "1 unread message";
    public static final String UNREAD_MESSAGES = "unread messages";
    public static final String UNREAD_MESSAGES_0 = "0 unread messages";
    public static final String UPDATED = "RuneScape has been updated!";
    public static final String USE_DIFFERENT_WORLD = "Please try using a different world.";
    public static final char VALID_CHARACTERS[] = {' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', ']'};
    public static final char VALID_NAME_CHARACTERS[] = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    public static final String WAIT_1_MINUTE = "Please wait 1 minute and try again.";
    public static final String WHI = "@whi@";
    public static final String WITHIN_MEMBERS = "You are standing in a members-only area.";
    public static final String WORLD = "@gre@World-";
    public static final String WORLD_FULL = "This world is full.";
    public static final String YESTERDAY = "yesterday";

    public static String to_asterisks(String s) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            b.append("*");
        }
        return b.toString();
    }

    public static String get_filtered(String s) {
        buffer.position = 0;
        put(s, buffer);
        int start = buffer.position;
        buffer.position = 0;
        return get_formatted(start, buffer);
    }

    public static String get_formatted(String s) {
        if (s.length() > 0) {
            char chars[] = s.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '_') {
                    chars[i] = ' ';
                    if (i + 1 < chars.length && chars[i + 1] >= 'a' && chars[i + 1] <= 'z') {
                        chars[i + 1] = (char) ((chars[i + 1] + 65) - 97);
                    }
                }
            }
            if (chars[0] >= 'a' && chars[0] <= 'z') {
                chars[0] = (char) ((chars[0] + 65) - 97);
            }
            return new String(chars);
        } else {
            return s;
        }
    }

    public static String get_formatted(int length, Buffer b) {
        int off = 0;
        int k = -1;
        for (int i = 0; i < length; i++) {
            int i1 = b.get_ubyte();
            int j1 = i1 >> 4 & 0xf;

            if (k == -1) {
                if (j1 < 13) {
                    FORMAT_BUFFER[off++] = VALID_CHARACTERS[j1];
                } else {
                    k = j1;
                }
            } else {
                FORMAT_BUFFER[off++] = VALID_CHARACTERS[((k << 4) + j1) - 195];
                k = -1;
            }

            j1 = i1 & 0xF;

            if (k == -1) {
                if (j1 < 13) {
                    FORMAT_BUFFER[off++] = VALID_CHARACTERS[j1];
                } else {
                    k = j1;
                }
            } else {
                FORMAT_BUFFER[off++] = VALID_CHARACTERS[((k << 4) + j1) - 195];
                k = -1;
            }
        }

        boolean capitalize = true;
        for (int i = 0; i < off; i++) {
            char c = FORMAT_BUFFER[i];

            if (capitalize && c >= 'a' && c <= 'z') {
                FORMAT_BUFFER[i] += '\uFFE0';
                capitalize = false;
            }

            if (c == '.' || c == '!' || c == '?') {
                capitalize = true;
            }
        }

        return new String(FORMAT_BUFFER, 0, off);
    }

    public static String get_address(int i) {
        return (i >> 24 & 0xff) + "." + (i >> 16 & 0xff) + "." + (i >> 8 & 0xff) + "." + (i & 0xff);
    }

    public static String get_formatted_string(long l) {
        return get_formatted(get_string(l));
    }

    public static long get_hash(String s) {
        s = s.toUpperCase();
        long l = 0L;
        for (int i = 0; i < s.length(); i++) {
            l = (l * 61L + (long) s.charAt(i)) - 32L;
            l = l + (l >> 56) & 0xffffffffffffffL;
        }
        return l;
    }

    public static long get_long(String s) {
        long l = 0L;
        for (int i = 0; i < s.length() && i < 12; i++) {
            char c = s.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z') {
                l += (1 + c) - 65;
            } else if (c >= 'a' && c <= 'z') {
                l += (1 + c) - 97;
            } else if (c >= '0' && c <= '9') {
                l += (27 + c) - 48;
            }
        }

        for (; l % 37L == 0L && l != 0L; l /= 37L) {
        }
        return l;
    }

    public static String get_string(long l) {
        if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L) {
            return INVALID_STRING;
        }
        if (l % 37L == 0L) {
            return INVALID_STRING;
        }
        int len = 0;
        char characters[] = new char[12];
        while (l != 0L) {
            long l1 = l;
            l /= 37L;
            characters[11 - len++] = VALID_NAME_CHARACTERS[(int) (l1 - l * 37L)];
        }
        return new String(characters, 12 - len, len);
    }

    public static boolean is_vowel(String s, int char_index) {
        if (char_index < 0 || char_index > s.length()) {
            return false;
        }
        char c = s.charAt(char_index);
        return c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
    }

    /**
     * Returns the local player instance's name if it exists, else the formatted login input username.
     *
     * @return the username.
     */
    public static String local_username() {
        if (Game.self != null && Game.self.name != null) {
            return Game.self.name;
        }
        return get_formatted(Game.username);
    }

    /**
     * Puts a formatted version of the provided string into the provided stream.
     *
     * @param s      the string.
     * @param buffer the stream.
     */
    public static void put(String s, Buffer buffer) {
        if (s.length() > 80) {
            s = s.substring(0, 80);
        }

        s = s.toLowerCase();

        int a = -1;
        for (int i = 0; i < s.length(); i++) {
            int b = 0;
            char c = s.charAt(i);
            for (int l = 0; l < VALID_CHARACTERS.length; l++) {
                if (c != VALID_CHARACTERS[l]) {
                    continue;
                }
                b = l;
                break;
            }

            if (b > 12) {
                b += 195;
            }

            if (a == -1) {
                if (b < 13) {
                    a = b;
                } else {
                    buffer.put_byte(b);
                }
            } else if (b < 13) {
                buffer.put_byte((a << 4) + b);
                a = -1;
            } else {
                buffer.put_byte((a << 4) + (b >> 4));
                a = b & 0xF;
            }
        }

        if (a != -1) {
            buffer.put_byte(a << 4);
        }
    }

}
