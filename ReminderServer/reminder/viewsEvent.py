import json
from random import randint

from django.shortcuts import render
from django.http import HttpResponse
from reminder.models import user, friend, event, eventMember, subEvent

rand_limit = 100000000

def createEvent(request):
    Title = request.POST["Title"]
    UserID = json.loads(request.POST["UserID"])
    EndTime = request.POST["EndTime"]
    Share = request.POST["Share"]
    subEvents = json.loads(request.POST["subEvents"])
    Permission = (Share == "1")
    Ret = {"errCode" : -1}

    EventID = randint(1, rand_limit)
    while (event.objects.filter(eventID = EventID)):
        EventID = randint(1, rand_limit)

    if (Share == "共享"):
        Permission = Permission | 1
    else:
        Permission = Permission | 0
    eventTmp = event.objects.create(eventID = EventID, title = Title, permission = Permission,
                                    endTime = EndTime, fin = 0, forked = 0)
    eventTmp.save()

    for user in UserID:
        eventMemberTmp = eventMember.objects.create(eventID = EventID, userID = user)
        eventMemberTmp.save()

    for subTitle in subEvents:
        subEventID = randint(1, rand_limit)
        while (subEvent.objects.filter(subEventID = subEventID)):
            subEventID = randint(1, rand_limit)
        subEventTmp = subEvent.objects.create(eventID = EventID, subEventID = subEventID, title = subTitle, fin = 0, note = "")
        subEventTmp.save()

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

    print(json.dumps(Ret))
    response = HttpResponse(json.dumps(Ret), content_type = 'application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response


def finEvent(request):
    EventID = request.POST["eventID"]
    Fin = request.POST["fin"]
    Ret = {"errCode": -1}

    EventTmp = event.objects.get(eventID = EventID)
    EventTmp.fin = Fin
    EventTmp.save()

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

    print(json.dumps(Ret))
    response = HttpResponse(json.dumps(Ret), content_type = 'application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response


def finSubEvent(request):
    SubEventID = request.POST["subEventID"]
    Fin = request.POST["fin"]
    note = json.loads(request.POST["note"])
    Ret = {"errCode": -1}

    subEventTmp = subEvent.objects.get(subEventID = SubEventID)
    subEventTmp.fin = Fin
    if (Fin == "1"):
        subEventTmp.note = note
    else:
        subEventTmp.note = ""
    subEventTmp.save()

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

    print(json.dumps(Ret))
    response = HttpResponse(json.dumps(Ret), content_type = 'application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response


def queryEvents(request):

    UserID = request.POST["UserID"]
    Ret = {"errCode" : -1}

    EventsList2 = []
#    //type error
    EventIDList = eventMember.objects.filter(userID = UserID)
    for eventID in EventIDList:
        EventsList = event.objects.filter(eventID = eventID.eventID)
        for EventTmp in EventsList:
            Tmp = {}
            Tmp["eventID"] = EventTmp.eventID
            Tmp["title"] = EventTmp.title
            Tmp["forked"] = EventTmp.forked
            Tmp["fin"] = EventTmp.fin
            Tmp["permission"] = EventTmp.permission
            EventsList2.append(Tmp)

    Ret["events"] = EventsList2
    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

    print(json.dumps(Ret))
    response = HttpResponse(json.dumps(Ret), content_type = 'application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response



def queryTopEvents(request):

    Ret = {"errCode" : -1}

    EventsList2 = []
    EventsList = event.objects.filter()
    EventsList = sorted(EventsList, key = lambda x : x.forked, reverse = True)
    for EventTmp in EventsList:
        if (EventTmp.permission == 0):
            continue
        Tmp = {}
        Tmp["eventID"] = EventTmp.eventID
        Tmp["title"] = EventTmp.title
        EventsList2.append(Tmp)

    EventsList2 = EventsList2[:10]
    Ret["events"] = EventsList2
    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

    print(json.dumps(Ret))
    response = HttpResponse(json.dumps(Ret), content_type = 'application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response



def querySingleEvent(request):

    EventID = request.POST["eventID"]
    Ret = {"errCode" : -1}

    EventsList = event.objects.filter(eventID = EventID)
    EventsList2 = []
    for EventTmp in EventsList:
        Tmp = {}
        Tmp["eventID"] = EventTmp.eventID
        Tmp["userID"] = []
        userIDList = eventMember.objects.filter(eventID = EventTmp.eventID)
        for UserID in userIDList:
            Tmp["userID"].append(UserID.userID)
        Tmp["title"] = EventTmp.title
        Tmp["permission"] = EventTmp.permission
        Tmp["endTime"] = EventTmp.endTime.strftime('%Y-%m-%d %H:%M')
        Tmp["fin"] = EventTmp.fin
        Tmp["forked"] = EventTmp.forked
        Tmp["subEvent"] = []
        subEventsList = subEvent.objects.filter(eventID = EventID)
        for subEventTmp in subEventsList:
            cc = subEventTmp.title
            if (subEventTmp.fin == 1):
                cc = cc + subEventTmp.note
            print(subEventTmp.fin, ' ', subEventTmp.note)
            Tmp["subEvent"].append((cc, subEventTmp.subEventID, subEventTmp.fin))
        EventsList2.append(Tmp)

    Ret["events"] = EventsList2
    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

    print(json.dumps(Ret))
    response = HttpResponse(json.dumps(Ret), content_type = 'application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response


def addForkedTime(request):
    EventID = request.POST["eventID"]
    Ret = {"errCode": -1}

    EventTmp = event.objects.get(eventID = EventID)
    EventTmp.forked = EventTmp.forked + 1
    EventTmp.save()

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

    print(json.dumps(Ret))
    response = HttpResponse(json.dumps(Ret), content_type = 'application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response
