import json
from random import randint

from django.shortcuts import render
from django.http import HttpResponse
from reminder.models import user, friend, event, eventRelationship, comment, link, linkComponent

rand_limit = 100000000;

def createEvent(request):
    Title = request.POST["Title"]
    UserID = request.POST["UserID"]
    Content = request.POST["Content"]
    StartTime = request.POST["StartTime"]
    EndTime = request.POST["EndTime"]
    Share = request.POST["Share"]
    print("StartTime : ", StartTime)
    print("EndTime : ", StartTime)
    Permission = 0
    Ret = {"errCode" : -1}

    EventID = randint(1, rand_limit)
    while (event.objects.filter(eventID = EventID)):
        EventID = randint(1, rand_limit)

    userTmp = user.objects.filter(userID=UserID)
    if (len(userTmp) > 0):
        if (Share == "共享"):
            Permission = Permission | 1
        else:
            Permission = Permission | 0
        eventTmp = event.objects.create(eventID = EventID, userID = UserID, title = Title, content = Content,
                                        permission = Permission, approve = 0, startTime = StartTime, endTime = EndTime, fin = 0, forked = 0)
        eventTmp.save()
    else:
        Ret["errCode"] = 1

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0
    return HttpResponse(json.dumps(Ret), content_type = 'application/json')


def changeEvent(request):
    EventID = request.POST["EventID"]
    Title = request.POST["Title"]
    UserID = request.POST["UserID"]
    Content = request.POST["Content"]
    StartTime = request.POST["StartTime"]
    EndTime = request.POST["EndTime"]
    Share = request.POST["Share"]
    print("StartTime : ", StartTime)
    print("EndTime : ", StartTime)
    Permission = 0
    Ret = {"errCode": -1}
    if (Share == "共享"):
        Permission = Permission | 1
    else:
        Permission = Permission | 0

    eventTmp = event.objects.get(eventID=EventID)
    eventTmp.title = Title
    eventTmp.content = Content
    eventTmp.startTime = StartTime
    eventTmp.endTime = EndTime
    eventTmp.permission = Permission

    eventTmp.save()

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0
    return HttpResponse(json.dumps(Ret), content_type='application/json')


def commentEvent(request):

    EventID = request.POST["EventID"]
    UserID = request.POST["UserID"]
    Content = request.POST["Content"]
    Ret = {"errCode" : -1}

    commentTmp = comment.objects.create(eventID = EventID, userID = UserID, content = Content)
    commentTmp.save()

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0
    return HttpResponse(json.dumps(Ret), content_type = 'application/json')


def deleteEvent(request):
    EventID = request.POST["EventID"]
    Ret = {"errCode": -1}

    event.objects.filter(eventID=EventID).delete();

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0
    return HttpResponse(json.dumps(Ret), content_type='application/json')


def queryEvents(request):

    UserID = request.POST["UserID"]
    Ret = {"errCode" : -1}

    EventsList = event.objects.filter(userID = UserID)
    EventsList2 = []
    for EventTmp in EventsList:
        Tmp = {}
        Tmp["eventID"] = EventTmp.eventID
        Tmp["permission"] = EventTmp.permission
        Tmp["approve"] = EventTmp.approve
        Tmp["fin"] = EventTmp.fin
        Tmp["userID"] = EventTmp.userID
        Tmp["title"] = EventTmp.title
        Tmp["content"] = EventTmp.content
        Tmp["startTime"] = EventTmp.startTime.strftime('%Y-%m-%d')
        Tmp["endTime"] = EventTmp.endTime.strftime('%Y-%m-%d')
        EventsList2.append(Tmp)

    Ret["events"] = EventsList2
    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0
    print(json.dumps(Ret))
    return HttpResponse(json.dumps(Ret), content_type = 'application/json')


def queryComments(request):

    UserID = request.POST["UserID"]
    Ret = {"errCode" : -1}

    EventsList = event.objects.filter(userID = UserID)
    CommentsList2 = []
    for EventTmp in EventsList:
        CommentsList = comment.objects.filter(eventID = EventTmp.eventID)
        for CommentTmp in CommentsList:
            Tmp = {}
            Tmp["eventID"] = CommentTmp.eventID
            Tmp["userID"] = CommentTmp.userID
            Tmp["title"] = EventTmp.title
            Tmp["content"] = CommentTmp.content
            CommentsList2.append(Tmp)

    Ret["comments"] = CommentsList2
    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0
    print(json.dumps(Ret))
    return HttpResponse(json.dumps(Ret), content_type = 'application/json')


def queryNearby(request):

    UserID = request.POST["UserID"]
    Ret = {"errCode" : -1}

    FriendsList = friend.objects.filter(uAID = UserID)
    EventsList2 = []
    for UserTmp in FriendsList:
        EventsList = event.objects.filter(userID = UserTmp.uBID)
        for EventTmp in EventsList:
            Tmp = {}
            Tmp["eventID"] = EventTmp.eventID
            Tmp["permission"] = EventTmp.permission
            Tmp["approve"] = EventTmp.approve
            Tmp["fin"] = EventTmp.fin
            Tmp["userID"] = EventTmp.userID
            Tmp["title"] = EventTmp.title
            Tmp["content"] = EventTmp.content
            Tmp["startTime"] = EventTmp.startTime.strftime('%Y-%m-%d')
            Tmp["endTime"] = EventTmp.endTime.strftime('%Y-%m-%d')
            EventsList2.append(Tmp)

    Ret["nearby"] = EventsList2
    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0
    print(json.dumps(Ret))
    return HttpResponse(json.dumps(Ret), content_type = 'application/json')

