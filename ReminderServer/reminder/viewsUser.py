import json
from django.shortcuts import render
from django.http import HttpResponse
from reminder.models import user, friend, event, comment, eventMember


def registe(request):
    userID = request.POST["userID"]
    password = request.POST["password"]
    Ret = {"errCode" : -1}

    userTmp = user.objects.filter(userID=userID)
    if (len(userTmp) > 0):
        Ret["errCode"] = 1
    else:
        userTmp = user.objects.create(userID = userID, password = password)
        userTmp.save()

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

    print(json.dumps(Ret))
    response = HttpResponse(json.dumps(Ret), content_type = 'application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response


def logIn(request):
    userID = request.POST["userID"]
    password = request.POST["password"]
    Ret = {"errCode" : -1}

    userTmp = user.objects.filter(userID = userID)
    if (len(userTmp) > 0):
        if (userTmp[0].password != password):
            Ret["errCode"] = 2
    else:
        Ret["errCode"] = 1

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

#    ip = request.META['REMOTE_ADDR']

    print(json.dumps(Ret))
    response = HttpResponse(json.dumps(Ret), content_type = 'application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response


def logOut(request):
    Ret = {"errCode" : -1}

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

    print(json.dumps(Ret))
    response = HttpResponse(json.dumps(Ret), content_type = 'application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response


def queryUsers(request):
    Ret = {"errCode" : -1}

    wildcard = request.POST["wildcard"]

    userList = user.objects.filter(userID__contains = wildcard)
    userList2 = []
    for userTmp in userList:
        userList2.append(userTmp.userID)
    Ret["userList"] = userList2

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

    print(json.dumps(Ret))
    response = HttpResponse(json.dumps(Ret), content_type = 'application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response



def updatePassword(request):
    Ret = {"errCode": -1}
    userID = request.POST["userID"]
    password = request.POST["password"]

    userTmp = user.objects.filter(userID = userID)
    userTmp[0].password = password
    userTmp[0].save()

    if (Ret["errCode"] == -1):
        Ret["errCode"] = 0

    print(json.dumps(Ret))
    response = HttpResponse(json.dumps(Ret), content_type = 'application/json')
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS"
    response["Access-Control-Max-Age"] = "1000"
    response["Access-Control-Allow-Headers"] = "*"
    return response
