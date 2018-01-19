# Generated by Django 2.0.1 on 2018-01-19 01:02

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('reminder', '0006_subevent_fin'),
    ]

    operations = [
        migrations.DeleteModel(
            name='comment',
        ),
        migrations.RemoveField(
            model_name='event',
            name='approve',
        ),
        migrations.RemoveField(
            model_name='event',
            name='content',
        ),
        migrations.AddField(
            model_name='subevent',
            name='note',
            field=models.CharField(default=0, max_length=100),
            preserve_default=False,
        ),
        migrations.AlterField(
            model_name='event',
            name='title',
            field=models.CharField(max_length=100),
        ),
        migrations.AlterField(
            model_name='eventmember',
            name='userID',
            field=models.CharField(max_length=100),
        ),
        migrations.AlterField(
            model_name='friend',
            name='uAID',
            field=models.CharField(max_length=100),
        ),
        migrations.AlterField(
            model_name='friend',
            name='uBID',
            field=models.CharField(max_length=100),
        ),
        migrations.AlterField(
            model_name='subevent',
            name='title',
            field=models.CharField(max_length=100),
        ),
        migrations.AlterField(
            model_name='user',
            name='password',
            field=models.CharField(max_length=100),
        ),
        migrations.AlterField(
            model_name='user',
            name='userID',
            field=models.CharField(max_length=100),
        ),
    ]