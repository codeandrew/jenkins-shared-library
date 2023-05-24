# How to add Webhook triggers to Jenkin

## BitBucket

To add a webhook trigger in Bitbucket to trigger a Jenkins pipeline on push events, you can follow these steps:

1. Open your Bitbucket repository in a web browser.
2. Go to the repository settings.
3. Select "Webhooks" from the left menu.
4. Click on the "Create webhook" or "Add webhook" button.
5. Configure the webhook settings:
   - URL: Enter the URL of your Jenkins server with the Bitbucket plugin endpoint, typically in the format `https://jenkins-server/bitbucket-hook/`.
   - Name: Provide a descriptive name for the webhook.
   - Events: Select "Repository push" or "Push" event.
   - Save or Add the webhook.

Once the webhook is set up, Bitbucket will send a notification to the provided Jenkins URL whenever a push event occurs in the repository. Jenkins will receive this notification and trigger the associated pipeline.

Make sure that your Jenkins server is accessible from the internet and reachable by Bitbucket's webhook requests. Additionally, ensure that you have the Bitbucket plugin installed and configured in your Jenkins server to handle the webhook events.

Please refer to the official documentation of Bitbucket and the Bitbucket plugin for more detailed instructions specific to your Bitbucket version and Jenkins setup.
