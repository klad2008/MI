"""ReminderServer URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin
from reminder import views, viewsUser, viewsFriends, viewsEvent

urlpatterns = [
    url(r'^admin/', admin.site.urls),
    url(r'^signin.html$', views.chatroom),
    url(r'^logIn/', viewsUser.logIn),
    url(r'^logOut/', viewsUser.logOut),
    url(r'^registe/', viewsUser.registe),
    url(r'^updatePassword/', viewsUser.updatePassword),
    url(r'^updateFriends/', viewsFriends.updateFriends),
    url(r'^queryFriends/', viewsFriends.queryFriends),
    url(r'^createEvent/', viewsEvent.createEvent),
    url(r'^changeEvent/', viewsEvent.changeEvent),
    url(r'^deleteEvent/', viewsEvent.deleteEvent),
    url(r'^commentEvent/', viewsEvent.commentEvent),
    url(r'^queryEvents/', viewsEvent.queryEvents),
    url(r'^queryComments/', viewsEvent.queryComments),
    url(r'^queryNearby/', viewsEvent.queryNearby),
]
