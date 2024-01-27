class Message {
    constructor(id, text, senderId, chatId) {
        this.id = id;
        this.text = text;
        this.senderId = senderId;
        this.chatId = chatId;
        this.timestamp = id !== null ? objectIdToTimestamp(id) : null;
        this.formatedTime = id !== null ? this.timestamp.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : null;
    }
    static fromJson(json) {
        return new Message(json.id, json.text, json.senderId, json.chatId)
    }
}

class Participant {

    constructor(chatId, personId) {
        this.chatId = chatId;
        this.personId = personId;
    }

    static fromJson(json) {
        return new Participant(json.chatId, json.personId);
    }

}

class Chat {
    constructor(chatId, type) {
        this.chatId = chatId;
        this.type = type;
    }
}

class SingleChat extends Chat {
    constructor(chatId, firstPersonId, secondPersonId, lastMessage) {
        super(chatId, "single_chat");
        this.firstPersonId = firstPersonId;
        this.secondPersonId = secondPersonId;
        this.lastMessage = lastMessage;
        this.othetPersonId = firstPersonId === id ? secondPersonId : firstPersonId;
    }

    static fromJson(json) {
        return new SingleChat(json.chatId, json.firstPersonId, json.secondPersonId, (json.lastMessage ? Message.fromJson(json.lastMessage) : null))
    }
}

class GroupChat extends Chat {
    constructor(chatId, name, ownerId, participantCount, lastMessage, hasProfilePhoto) {
        super(chatId, "group_chat");
        this.name = name;
        this.ownerId = ownerId;
        this.participantCount = participantCount;
        this.lastMessage = lastMessage;
        this.hasProfilePhoto = hasProfilePhoto;
    }
    static fromJson(json) {
        return new GroupChat(json.chatId, json.name, json.ownerId, json.participantCount, (json.lastMessage ? Message.fromJson(json.lastMessage) : null), json.hasProfilePhoto)
    }
}

class Person {
    constructor(id, username, gender, status, hasProfilePhoto) {
        this.id = id;
        this.username = username;
        this.gender = gender;
        this.status = status;
        this.hasProfilePhoto = hasProfilePhoto;
    }

    static fromJson(json) {
        return new Person(json.id, json.username, json.gender, json.status, json.hasProfilePhoto);
    }
}

class ProfilePhoto {
    constructor(image) {
        this.url = URL.createObjectURL(image);
    }

}































