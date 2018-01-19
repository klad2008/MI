import json
from django.shortcuts import render
from django.http import HttpResponse
from reminder.models import user, friend, event

def signin(request):
    return render(request, "signin.html")
