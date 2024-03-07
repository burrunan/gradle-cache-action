@file:JsModule("@octokit/webhooks")
@file:JsQualifier("EventPayloads")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package octokit.webhooks

external interface WebhookPayloadWorkflowRunSender {
    var avatar_url: String
    var events_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var gravatar_id: String
    var html_url: String
    var id: Number
    var login: String
    var node_id: String
    var organizations_url: String
    var received_events_url: String
    var repos_url: String
    var site_admin: Boolean
    var starred_url: String
    var subscriptions_url: String
    var type: String
    var url: String
}

external interface WebhookPayloadWorkflowRunOrganization {
    var avatar_url: String
    var description: String
    var events_url: String
    var hooks_url: String
    var id: Number
    var issues_url: String
    var login: String
    var members_url: String
    var node_id: String
    var public_members_url: String
    var repos_url: String
    var url: String
}

external interface WebhookPayloadWorkflowRun {
    var action: String
    var organization: WebhookPayloadWorkflowRunOrganization
    var repository: PayloadRepository
    var sender: WebhookPayloadWorkflowRunSender
}

external interface WebhookPayloadWorkflowDispatchSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadWorkflowDispatchOrganization {
    var login: String
    var id: Number
    var node_id: String
    var url: String
    var repos_url: String
    var events_url: String
    var hooks_url: String
    var issues_url: String
    var members_url: String
    var public_members_url: String
    var avatar_url: String
    var description: String
}

external interface WebhookPayloadWorkflowDispatchInputs

external interface WebhookPayloadWorkflowDispatch {
    var inputs: WebhookPayloadWorkflowDispatchInputs
    var ref: String
    var repository: PayloadRepository
    var organization: WebhookPayloadWorkflowDispatchOrganization
    var sender: WebhookPayloadWorkflowDispatchSender
    var workflow: String
}

external interface WebhookPayloadWatchSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadWatch {
    var action: String
    var repository: PayloadRepository
    var sender: WebhookPayloadWatchSender
}

external interface WebhookPayloadTeamAddSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadTeamAddOrganization {
    var login: String
    var id: Number
    var node_id: String
    var url: String
    var repos_url: String
    var events_url: String
    var hooks_url: String
    var issues_url: String
    var members_url: String
    var public_members_url: String
    var avatar_url: String
    var description: String
}

external interface WebhookPayloadTeamAddTeam {
    var name: String
    var id: Number
    var node_id: String
    var slug: String
    var description: String
    var privacy: String
    var url: String
    var html_url: String
    var members_url: String
    var repositories_url: String
    var permission: String
}

external interface WebhookPayloadTeamAdd {
    var team: WebhookPayloadTeamAddTeam
    var repository: PayloadRepository
    var organization: WebhookPayloadTeamAddOrganization
    var sender: WebhookPayloadTeamAddSender
}

external interface WebhookPayloadTeamChanges

external interface WebhookPayloadTeamSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadTeamOrganization {
    var login: String
    var id: Number
    var node_id: String
    var url: String
    var repos_url: String
    var events_url: String
    var hooks_url: String
    var issues_url: String
    var members_url: String
    var public_members_url: String
    var avatar_url: String
    var description: String
}

external interface PayloadRepositoryPermissions {
    var pull: Boolean
    var push: Boolean
    var admin: Boolean
}

external interface WebhookPayloadTeamTeam {
    var name: String
    var id: Number
    var node_id: String
    var slug: String
    var description: String?
    var privacy: String
    var url: String
    var html_url: String
    var members_url: String
    var repositories_url: String
    var permission: String
}

external interface WebhookPayloadTeam {
    var action: String
    var team: WebhookPayloadTeamTeam
    var repository: PayloadRepository?
    var organization: WebhookPayloadTeamOrganization
    var sender: WebhookPayloadTeamSender
    var changes: WebhookPayloadTeamChanges?
}

external interface WebhookPayloadStatusSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadStatusBranchesItemCommit {
    var sha: String
    var url: String
}

external interface WebhookPayloadStatusBranchesItem {
    var name: String
    var commit: WebhookPayloadStatusBranchesItemCommit
    var protected: Boolean
}

external interface WebhookPayloadStatusCommitCommitter {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadStatusCommitAuthor {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadStatusCommitCommitVerification {
    var verified: Boolean
    var reason: String
    var signature: String
    var payload: String
}

external interface WebhookPayloadStatusCommitCommitTree {
    var sha: String
    var url: String
}

external interface WebhookPayloadStatusCommitCommitCommitter {
    var name: String
    var email: String
    var date: String
}

external interface WebhookPayloadStatusCommitCommitAuthor {
    var name: String
    var email: String
    var date: String
}

external interface WebhookPayloadStatusCommitCommit {
    var author: WebhookPayloadStatusCommitCommitAuthor
    var committer: WebhookPayloadStatusCommitCommitCommitter
    var message: String
    var tree: WebhookPayloadStatusCommitCommitTree
    var url: String
    var comment_count: Number
    var verification: WebhookPayloadStatusCommitCommitVerification
}

external interface WebhookPayloadStatusCommit {
    var sha: String
    var node_id: String
    var commit: WebhookPayloadStatusCommitCommit
    var url: String
    var html_url: String
    var comments_url: String
    var author: WebhookPayloadStatusCommitAuthor
    var committer: WebhookPayloadStatusCommitCommitter
    var parents: Array<Any>
}

external interface WebhookPayloadStatus {
    var id: Number
    var sha: String
    var name: String
    var target_url: Any?
    var context: String
    var description: Any?
    var state: String
    var commit: WebhookPayloadStatusCommit
    var branches: Array<WebhookPayloadStatusBranchesItem>
    var created_at: String
    var updated_at: String
    var repository: PayloadRepository
    var sender: WebhookPayloadStatusSender
}

external interface WebhookPayloadStarSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadStar {
    var action: String
    var starred_at: String?
    var repository: PayloadRepository
    var sender: WebhookPayloadStarSender
}

external interface WebhookPayloadSponsorshipChangesTierFrom {
    var node_id: String
    var created_at: String
    var description: String
    var monthly_price_in_cents: Number
    var monthly_price_in_dollars: Number
    var name: String
}

external interface WebhookPayloadSponsorshipChangesTier {
    var from: WebhookPayloadSponsorshipChangesTierFrom
}

external interface WebhookPayloadSponsorshipChanges {
    var tier: WebhookPayloadSponsorshipChangesTier
}

external interface WebhookPayloadSponsorshipSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadSponsorshipSponsorshipTier {
    var node_id: String
    var created_at: String
    var description: String
    var monthly_price_in_cents: Number
    var monthly_price_in_dollars: Number
    var name: String
}

external interface WebhookPayloadSponsorshipSponsorshipSponsor {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadSponsorshipSponsorshipSponsorable {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadSponsorshipSponsorship {
    var node_id: String
    var created_at: String
    var sponsorable: WebhookPayloadSponsorshipSponsorshipSponsorable
    var sponsor: WebhookPayloadSponsorshipSponsorshipSponsor
    var privacy_level: String
    var tier: WebhookPayloadSponsorshipSponsorshipTier
}

external interface WebhookPayloadSponsorship {
    var action: String
    var sponsorship: WebhookPayloadSponsorshipSponsorship
    var sender: WebhookPayloadSponsorshipSender
    var changes: WebhookPayloadSponsorshipChanges?
    var effective_date: String?
}

external interface WebhookPayloadSecurityAdvisorySecurityAdvisoryVulnerabilitiesItemFirstPatchedVersion {
    var identifier: String
}

external interface WebhookPayloadSecurityAdvisorySecurityAdvisoryVulnerabilitiesItemPackage {
    var ecosystem: String
    var name: String
}

external interface WebhookPayloadSecurityAdvisorySecurityAdvisoryVulnerabilitiesItem {
    var `package`: WebhookPayloadSecurityAdvisorySecurityAdvisoryVulnerabilitiesItemPackage
    var severity: String
    var vulnerable_version_range: String
    var first_patched_version: WebhookPayloadSecurityAdvisorySecurityAdvisoryVulnerabilitiesItemFirstPatchedVersion
}

external interface WebhookPayloadSecurityAdvisorySecurityAdvisoryReferencesItem {
    var url: String
}

external interface WebhookPayloadSecurityAdvisorySecurityAdvisoryIdentifiersItem {
    var value: String
    var type: String
}

external interface WebhookPayloadSecurityAdvisorySecurityAdvisory {
    var ghsa_id: String
    var summary: String
    var description: String
    var severity: String
    var identifiers: Array<WebhookPayloadSecurityAdvisorySecurityAdvisoryIdentifiersItem>
    var references: Array<WebhookPayloadSecurityAdvisorySecurityAdvisoryReferencesItem>
    var published_at: String
    var updated_at: String
    var withdrawn_at: Any?
    var vulnerabilities: Array<WebhookPayloadSecurityAdvisorySecurityAdvisoryVulnerabilitiesItem>
}

external interface WebhookPayloadSecurityAdvisory {
    var action: String
    var security_advisory: WebhookPayloadSecurityAdvisorySecurityAdvisory
}

external interface WebhookPayloadRepositoryVulnerabilityAlertAlertDismisser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadRepositoryVulnerabilityAlertSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadRepositoryVulnerabilityAlertAlert {
    var id: Number
    var affected_range: String
    var affected_package_name: String
    var external_reference: String
    var external_identifier: String
    var fixed_in: String
    var dismisser: WebhookPayloadRepositoryVulnerabilityAlertAlertDismisser?
    var dismiss_reason: String?
    var dismissed_at: String?
}

external interface WebhookPayloadRepositoryVulnerabilityAlert {
    var action: String
    var alert: WebhookPayloadRepositoryVulnerabilityAlertAlert
    var repository: PayloadRepository?
    var sender: WebhookPayloadRepositoryVulnerabilityAlertSender?
}

external interface WebhookPayloadRepositoryImportSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadRepositoryImportOrganization {
    var login: String
    var id: Number
    var node_id: String
    var url: String
    var repos_url: String
    var events_url: String
    var hooks_url: String
    var issues_url: String
    var members_url: String
    var public_members_url: String
    var avatar_url: String
    var description: String
}

external interface WebhookPayloadRepositoryImport {
    var status: String
    var repository: PayloadRepository
    var organization: WebhookPayloadRepositoryImportOrganization
    var sender: WebhookPayloadRepositoryImportSender
}

external interface WebhookPayloadRepositoryOrganization {
    var login: String
    var id: Number
    var node_id: String
    var url: String
    var repos_url: String
    var events_url: String
    var hooks_url: String
    var issues_url: String
    var members_url: String
    var public_members_url: String
    var avatar_url: String
    var description: String
}

external interface WebhookPayloadRepositorySender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadRepository {
    var action: String
    var repository: PayloadRepository
    var sender: WebhookPayloadRepositorySender
    var organization: WebhookPayloadRepositoryOrganization?
}

external interface WebhookPayloadRepositoryDispatchInstallation {
    var id: Number
    var node_id: String
}

external interface WebhookPayloadRepositoryDispatchSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadRepositoryDispatchOrganization {
    var login: String
    var id: Number
    var node_id: String
    var url: String
    var repos_url: String
    var events_url: String
    var hooks_url: String
    var issues_url: String
    var members_url: String
    var public_members_url: String
    var avatar_url: String
    var description: String
}

external interface WebhookPayloadRepositoryDispatchClientPayload

external interface WebhookPayloadRepositoryDispatch {
    var action: String
    var branch: String
    var client_payload: WebhookPayloadRepositoryDispatchClientPayload
    var repository: PayloadRepository
    var organization: WebhookPayloadRepositoryDispatchOrganization
    var sender: WebhookPayloadRepositoryDispatchSender
    var installation: WebhookPayloadRepositoryDispatchInstallation
}

external interface WebhookPayloadReleaseSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadReleaseReleaseAuthor {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadReleaseRelease {
    var url: String
    var assets_url: String
    var upload_url: String
    var html_url: String
    var id: Number
    var node_id: String
    var tag_name: String
    var target_commitish: String
    var name: Any?
    var draft: Boolean
    var author: WebhookPayloadReleaseReleaseAuthor
    var prerelease: Boolean
    var created_at: String
    var published_at: String
    var assets: Array<Any>
    var tarball_url: String
    var zipball_url: String
    var body: Any?
}

external interface WebhookPayloadRelease {
    var action: String
    var release: WebhookPayloadReleaseRelease
    var repository: PayloadRepository
    var sender: WebhookPayloadReleaseSender
}

external interface WebhookPayloadPushSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPushPusher {
    var name: String
    var email: String
}

external interface WebhookPayloadPush {
    var ref: String
    var before: String
    var after: String
    var created: Boolean
    var deleted: Boolean
    var forced: Boolean
    var base_ref: Any?
    var compare: String
    var commits: Array<Any>
    var head_commit: Any?
    var repository: PayloadRepository
    var pusher: WebhookPayloadPushPusher
    var sender: WebhookPayloadPushSender
}

external interface WebhookPayloadPullRequestReviewCommentSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestLinksStatuses {
    var href: String
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestLinksCommits {
    var href: String
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestLinksReviewComment {
    var href: String
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestLinksReviewComments {
    var href: String
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestLinksComments {
    var href: String
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestLinksIssue {
    var href: String
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestLinksHtml {
    var href: String
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestLinksSelf {
    var href: String
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestLinks {
    var self: WebhookPayloadPullRequestReviewCommentPullRequestLinksSelf
    var html: WebhookPayloadPullRequestReviewCommentPullRequestLinksHtml
    var issue: WebhookPayloadPullRequestReviewCommentPullRequestLinksIssue
    var comments: WebhookPayloadPullRequestReviewCommentPullRequestLinksComments
    var review_comments: WebhookPayloadPullRequestReviewCommentPullRequestLinksReviewComments
    var review_comment: WebhookPayloadPullRequestReviewCommentPullRequestLinksReviewComment
    var commits: WebhookPayloadPullRequestReviewCommentPullRequestLinksCommits
    var statuses: WebhookPayloadPullRequestReviewCommentPullRequestLinksStatuses
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestBaseRepoOwner {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestBaseRepo {
    var id: Number
    var node_id: String
    var name: String
    var full_name: String
    var private: Boolean
    var owner: WebhookPayloadPullRequestReviewCommentPullRequestBaseRepoOwner
    var html_url: String
    var description: Any?
    var fork: Boolean
    var url: String
    var forks_url: String
    var keys_url: String
    var collaborators_url: String
    var teams_url: String
    var hooks_url: String
    var issue_events_url: String
    var events_url: String
    var assignees_url: String
    var branches_url: String
    var tags_url: String
    var blobs_url: String
    var git_tags_url: String
    var git_refs_url: String
    var trees_url: String
    var statuses_url: String
    var languages_url: String
    var stargazers_url: String
    var contributors_url: String
    var subscribers_url: String
    var subscription_url: String
    var commits_url: String
    var git_commits_url: String
    var comments_url: String
    var issue_comment_url: String
    var contents_url: String
    var compare_url: String
    var merges_url: String
    var archive_url: String
    var downloads_url: String
    var issues_url: String
    var pulls_url: String
    var milestones_url: String
    var notifications_url: String
    var labels_url: String
    var releases_url: String
    var deployments_url: String
    var created_at: String
    var updated_at: String
    var pushed_at: String
    var git_url: String
    var ssh_url: String
    var clone_url: String
    var svn_url: String
    var homepage: Any?
    var size: Number
    var stargazers_count: Number
    var watchers_count: Number
    var language: String
    var has_issues: Boolean
    var has_projects: Boolean
    var has_downloads: Boolean
    var has_wiki: Boolean
    var has_pages: Boolean
    var forks_count: Number
    var mirror_url: Any?
    var archived: Boolean
    var disabled: Boolean
    var open_issues_count: Number
    var license: Any?
    var forks: Number
    var open_issues: Number
    var watchers: Number
    var default_branch: String
    var allow_squash_merge: Boolean?
    var allow_merge_commit: Boolean?
    var allow_rebase_merge: Boolean?
    var delete_branch_on_merge: Boolean?
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestBaseUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestBase {
    var label: String
    var ref: String
    var sha: String
    var user: WebhookPayloadPullRequestReviewCommentPullRequestBaseUser
    var repo: WebhookPayloadPullRequestReviewCommentPullRequestBaseRepo
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestHeadRepoOwner {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestHeadRepo {
    var id: Number
    var node_id: String
    var name: String
    var full_name: String
    var private: Boolean
    var owner: WebhookPayloadPullRequestReviewCommentPullRequestHeadRepoOwner
    var html_url: String
    var description: Any?
    var fork: Boolean
    var url: String
    var forks_url: String
    var keys_url: String
    var collaborators_url: String
    var teams_url: String
    var hooks_url: String
    var issue_events_url: String
    var events_url: String
    var assignees_url: String
    var branches_url: String
    var tags_url: String
    var blobs_url: String
    var git_tags_url: String
    var git_refs_url: String
    var trees_url: String
    var statuses_url: String
    var languages_url: String
    var stargazers_url: String
    var contributors_url: String
    var subscribers_url: String
    var subscription_url: String
    var commits_url: String
    var git_commits_url: String
    var comments_url: String
    var issue_comment_url: String
    var contents_url: String
    var compare_url: String
    var merges_url: String
    var archive_url: String
    var downloads_url: String
    var issues_url: String
    var pulls_url: String
    var milestones_url: String
    var notifications_url: String
    var labels_url: String
    var releases_url: String
    var deployments_url: String
    var created_at: String
    var updated_at: String
    var pushed_at: String
    var git_url: String
    var ssh_url: String
    var clone_url: String
    var svn_url: String
    var homepage: Any?
    var size: Number
    var stargazers_count: Number
    var watchers_count: Number
    var language: String
    var has_issues: Boolean
    var has_projects: Boolean
    var has_downloads: Boolean
    var has_wiki: Boolean
    var has_pages: Boolean
    var forks_count: Number
    var mirror_url: Any?
    var archived: Boolean
    var disabled: Boolean
    var open_issues_count: Number
    var license: Any?
    var forks: Number
    var open_issues: Number
    var watchers: Number
    var default_branch: String
    var allow_squash_merge: Boolean?
    var allow_merge_commit: Boolean?
    var allow_rebase_merge: Boolean?
    var delete_branch_on_merge: Boolean?
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestHeadUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestHead {
    var label: String
    var ref: String
    var sha: String
    var user: WebhookPayloadPullRequestReviewCommentPullRequestHeadUser
    var repo: WebhookPayloadPullRequestReviewCommentPullRequestHeadRepo
}

external interface WebhookPayloadPullRequestReviewCommentPullRequestUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewCommentPullRequest {
    var url: String
    var id: Number
    var node_id: String
    var html_url: String
    var diff_url: String
    var patch_url: String
    var issue_url: String
    var number: Number
    var state: String
    var locked: Boolean
    var title: String
    var user: WebhookPayloadPullRequestReviewCommentPullRequestUser
    var body: String
    var created_at: String
    var updated_at: String
    var closed_at: Any?
    var merged_at: Any?
    var merge_commit_sha: String
    var assignee: Any?
    var assignees: Array<Any>
    var requested_reviewers: Array<Any>
    var requested_teams: Array<Any>
    var labels: Array<Any>
    var milestone: Any?
    var commits_url: String
    var review_comments_url: String
    var review_comment_url: String
    var comments_url: String
    var statuses_url: String
    var head: WebhookPayloadPullRequestReviewCommentPullRequestHead
    var base: WebhookPayloadPullRequestReviewCommentPullRequestBase
    var _links: WebhookPayloadPullRequestReviewCommentPullRequestLinks
    var author_association: String
}

external interface WebhookPayloadPullRequestReviewCommentCommentLinksPullRequest {
    var href: String
}

external interface WebhookPayloadPullRequestReviewCommentCommentLinksHtml {
    var href: String
}

external interface WebhookPayloadPullRequestReviewCommentCommentLinksSelf {
    var href: String
}

external interface WebhookPayloadPullRequestReviewCommentCommentLinks {
    var self: WebhookPayloadPullRequestReviewCommentCommentLinksSelf
    var html: WebhookPayloadPullRequestReviewCommentCommentLinksHtml
    var pull_request: WebhookPayloadPullRequestReviewCommentCommentLinksPullRequest
}

external interface WebhookPayloadPullRequestReviewCommentCommentUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewCommentComment {
    var url: String
    var pull_request_review_id: Number
    var id: Number
    var node_id: String
    var diff_hunk: String
    var path: String
    var position: Number
    var original_position: Number
    var commit_id: String
    var original_commit_id: String
    var user: WebhookPayloadPullRequestReviewCommentCommentUser
    var body: String
    var created_at: String
    var updated_at: String
    var html_url: String
    var pull_request_url: String
    var author_association: String
    var _links: WebhookPayloadPullRequestReviewCommentCommentLinks
}

external interface WebhookPayloadPullRequestReviewComment {
    var action: String
    var comment: WebhookPayloadPullRequestReviewCommentComment
    var pull_request: WebhookPayloadPullRequestReviewCommentPullRequest
    var repository: PayloadRepository
    var sender: WebhookPayloadPullRequestReviewCommentSender
}

external interface WebhookPayloadPullRequestReviewSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewPullRequestLinksStatuses {
    var href: String
}

external interface WebhookPayloadPullRequestReviewPullRequestLinksCommits {
    var href: String
}

external interface WebhookPayloadPullRequestReviewPullRequestLinksReviewComment {
    var href: String
}

external interface WebhookPayloadPullRequestReviewPullRequestLinksReviewComments {
    var href: String
}

external interface WebhookPayloadPullRequestReviewPullRequestLinksComments {
    var href: String
}

external interface WebhookPayloadPullRequestReviewPullRequestLinksIssue {
    var href: String
}

external interface WebhookPayloadPullRequestReviewPullRequestLinksHtml {
    var href: String
}

external interface WebhookPayloadPullRequestReviewPullRequestLinksSelf {
    var href: String
}

external interface WebhookPayloadPullRequestReviewPullRequestLinks {
    var self: WebhookPayloadPullRequestReviewPullRequestLinksSelf
    var html: WebhookPayloadPullRequestReviewPullRequestLinksHtml
    var issue: WebhookPayloadPullRequestReviewPullRequestLinksIssue
    var comments: WebhookPayloadPullRequestReviewPullRequestLinksComments
    var review_comments: WebhookPayloadPullRequestReviewPullRequestLinksReviewComments
    var review_comment: WebhookPayloadPullRequestReviewPullRequestLinksReviewComment
    var commits: WebhookPayloadPullRequestReviewPullRequestLinksCommits
    var statuses: WebhookPayloadPullRequestReviewPullRequestLinksStatuses
}

external interface WebhookPayloadPullRequestReviewPullRequestBaseRepoOwner {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewPullRequestBaseRepo {
    var id: Number
    var node_id: String
    var name: String
    var full_name: String
    var private: Boolean
    var owner: WebhookPayloadPullRequestReviewPullRequestBaseRepoOwner
    var html_url: String
    var description: Any?
    var fork: Boolean
    var url: String
    var forks_url: String
    var keys_url: String
    var collaborators_url: String
    var teams_url: String
    var hooks_url: String
    var issue_events_url: String
    var events_url: String
    var assignees_url: String
    var branches_url: String
    var tags_url: String
    var blobs_url: String
    var git_tags_url: String
    var git_refs_url: String
    var trees_url: String
    var statuses_url: String
    var languages_url: String
    var stargazers_url: String
    var contributors_url: String
    var subscribers_url: String
    var subscription_url: String
    var commits_url: String
    var git_commits_url: String
    var comments_url: String
    var issue_comment_url: String
    var contents_url: String
    var compare_url: String
    var merges_url: String
    var archive_url: String
    var downloads_url: String
    var issues_url: String
    var pulls_url: String
    var milestones_url: String
    var notifications_url: String
    var labels_url: String
    var releases_url: String
    var deployments_url: String
    var created_at: String
    var updated_at: String
    var pushed_at: String
    var git_url: String
    var ssh_url: String
    var clone_url: String
    var svn_url: String
    var homepage: Any?
    var size: Number
    var stargazers_count: Number
    var watchers_count: Number
    var language: String
    var has_issues: Boolean
    var has_projects: Boolean
    var has_downloads: Boolean
    var has_wiki: Boolean
    var has_pages: Boolean
    var forks_count: Number
    var mirror_url: Any?
    var archived: Boolean
    var disabled: Boolean
    var open_issues_count: Number
    var license: Any?
    var forks: Number
    var open_issues: Number
    var watchers: Number
    var default_branch: String
    var allow_squash_merge: Boolean?
    var allow_merge_commit: Boolean?
    var allow_rebase_merge: Boolean?
    var delete_branch_on_merge: Boolean?
}

external interface WebhookPayloadPullRequestReviewPullRequestBaseUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewPullRequestBase {
    var label: String
    var ref: String
    var sha: String
    var user: WebhookPayloadPullRequestReviewPullRequestBaseUser
    var repo: WebhookPayloadPullRequestReviewPullRequestBaseRepo
}

external interface WebhookPayloadPullRequestReviewPullRequestHeadRepoOwner {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewPullRequestHeadRepo {
    var id: Number
    var node_id: String
    var name: String
    var full_name: String
    var private: Boolean
    var owner: WebhookPayloadPullRequestReviewPullRequestHeadRepoOwner
    var html_url: String
    var description: Any?
    var fork: Boolean
    var url: String
    var forks_url: String
    var keys_url: String
    var collaborators_url: String
    var teams_url: String
    var hooks_url: String
    var issue_events_url: String
    var events_url: String
    var assignees_url: String
    var branches_url: String
    var tags_url: String
    var blobs_url: String
    var git_tags_url: String
    var git_refs_url: String
    var trees_url: String
    var statuses_url: String
    var languages_url: String
    var stargazers_url: String
    var contributors_url: String
    var subscribers_url: String
    var subscription_url: String
    var commits_url: String
    var git_commits_url: String
    var comments_url: String
    var issue_comment_url: String
    var contents_url: String
    var compare_url: String
    var merges_url: String
    var archive_url: String
    var downloads_url: String
    var issues_url: String
    var pulls_url: String
    var milestones_url: String
    var notifications_url: String
    var labels_url: String
    var releases_url: String
    var deployments_url: String
    var created_at: String
    var updated_at: String
    var pushed_at: String
    var git_url: String
    var ssh_url: String
    var clone_url: String
    var svn_url: String
    var homepage: Any?
    var size: Number
    var stargazers_count: Number
    var watchers_count: Number
    var language: String
    var has_issues: Boolean
    var has_projects: Boolean
    var has_downloads: Boolean
    var has_wiki: Boolean
    var has_pages: Boolean
    var forks_count: Number
    var mirror_url: Any?
    var archived: Boolean
    var disabled: Boolean
    var open_issues_count: Number
    var license: Any?
    var forks: Number
    var open_issues: Number
    var watchers: Number
    var default_branch: String
    var allow_squash_merge: Boolean?
    var allow_merge_commit: Boolean?
    var allow_rebase_merge: Boolean?
    var delete_branch_on_merge: Boolean?
}

external interface WebhookPayloadPullRequestReviewPullRequestHeadUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewPullRequestHead {
    var label: String
    var ref: String
    var sha: String
    var user: WebhookPayloadPullRequestReviewPullRequestHeadUser
    var repo: WebhookPayloadPullRequestReviewPullRequestHeadRepo
}

external interface WebhookPayloadPullRequestReviewPullRequestUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewPullRequest {
    var url: String
    var id: Number
    var node_id: String
    var html_url: String
    var diff_url: String
    var patch_url: String
    var issue_url: String
    var number: Number
    var state: String
    var locked: Boolean
    var title: String
    var user: WebhookPayloadPullRequestReviewPullRequestUser
    var body: String
    var created_at: String
    var updated_at: String
    var closed_at: Any?
    var merged_at: Any?
    var merge_commit_sha: String
    var assignee: Any?
    var assignees: Array<Any>
    var requested_reviewers: Array<Any>
    var requested_teams: Array<Any>
    var labels: Array<Any>
    var milestone: Any?
    var commits_url: String
    var review_comments_url: String
    var review_comment_url: String
    var comments_url: String
    var statuses_url: String
    var head: WebhookPayloadPullRequestReviewPullRequestHead
    var base: WebhookPayloadPullRequestReviewPullRequestBase
    var _links: WebhookPayloadPullRequestReviewPullRequestLinks
    var author_association: String
}

external interface WebhookPayloadPullRequestReviewReviewLinksPullRequest {
    var href: String
}

external interface WebhookPayloadPullRequestReviewReviewLinksHtml {
    var href: String
}

external interface WebhookPayloadPullRequestReviewReviewLinks {
    var html: WebhookPayloadPullRequestReviewReviewLinksHtml
    var pull_request: WebhookPayloadPullRequestReviewReviewLinksPullRequest
}

external interface WebhookPayloadPullRequestReviewReviewUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestReviewReview {
    var id: Number
    var node_id: String
    var user: WebhookPayloadPullRequestReviewReviewUser
    var body: Any?
    var commit_id: String
    var submitted_at: String
    var state: String
    var html_url: String
    var pull_request_url: String
    var author_association: String
    var _links: WebhookPayloadPullRequestReviewReviewLinks
}

external interface WebhookPayloadPullRequestReview {
    var action: String
    var review: WebhookPayloadPullRequestReviewReview
    var pull_request: WebhookPayloadPullRequestReviewPullRequest
    var repository: PayloadRepository
    var sender: WebhookPayloadPullRequestReviewSender
}

external interface WebhookPayloadPullRequestLabel {
    var id: Number
    var node_id: String
    var url: String
    var name: String
    var color: String
    var default: Boolean
}

external interface WebhookPayloadPullRequestPullRequestMilestoneCreator {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface `T$75` {
    var url: String
    var html_url: String
    var labels_url: String
    var id: Number
    var node_id: String
    var number: Number
    var title: String
    var description: String
    var creator: WebhookPayloadPullRequestPullRequestMilestoneCreator
    var open_issues: Number
    var closed_issues: Number
    var state: String
    var created_at: String
    var updated_at: String
    var due_on: String
    var closed_at: String
}

external interface WebhookPayloadPullRequestPullRequestLabelsItem {
    var id: Number
    var node_id: String
    var url: String
    var name: String
    var color: String
    var default: Boolean
}

external interface `T$76` {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestInstallation {
    var id: Number
    var node_id: String
}

external interface WebhookPayloadPullRequestAssignee {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestPullRequestAssigneesItem {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestPullRequestLinksStatuses {
    var href: String
}

external interface WebhookPayloadPullRequestPullRequestLinksCommits {
    var href: String
}

external interface WebhookPayloadPullRequestPullRequestLinksReviewComment {
    var href: String
}

external interface WebhookPayloadPullRequestPullRequestLinksReviewComments {
    var href: String
}

external interface WebhookPayloadPullRequestPullRequestLinksComments {
    var href: String
}

external interface WebhookPayloadPullRequestPullRequestLinksIssue {
    var href: String
}

external interface WebhookPayloadPullRequestPullRequestLinksHtml {
    var href: String
}

external interface WebhookPayloadPullRequestPullRequestLinksSelf {
    var href: String
}

external interface WebhookPayloadPullRequestPullRequestLinks {
    var self: WebhookPayloadPullRequestPullRequestLinksSelf
    var html: WebhookPayloadPullRequestPullRequestLinksHtml
    var issue: WebhookPayloadPullRequestPullRequestLinksIssue
    var comments: WebhookPayloadPullRequestPullRequestLinksComments
    var review_comments: WebhookPayloadPullRequestPullRequestLinksReviewComments
    var review_comment: WebhookPayloadPullRequestPullRequestLinksReviewComment
    var commits: WebhookPayloadPullRequestPullRequestLinksCommits
    var statuses: WebhookPayloadPullRequestPullRequestLinksStatuses
}

external interface WebhookPayloadPullRequestPullRequestBaseRepoOwner {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestPullRequestBaseRepo {
    var id: Number
    var node_id: String
    var name: String
    var full_name: String
    var private: Boolean
    var owner: WebhookPayloadPullRequestPullRequestBaseRepoOwner
    var html_url: String
    var description: Any?
    var fork: Boolean
    var url: String
    var forks_url: String
    var keys_url: String
    var collaborators_url: String
    var teams_url: String
    var hooks_url: String
    var issue_events_url: String
    var events_url: String
    var assignees_url: String
    var branches_url: String
    var tags_url: String
    var blobs_url: String
    var git_tags_url: String
    var git_refs_url: String
    var trees_url: String
    var statuses_url: String
    var languages_url: String
    var stargazers_url: String
    var contributors_url: String
    var subscribers_url: String
    var subscription_url: String
    var commits_url: String
    var git_commits_url: String
    var comments_url: String
    var issue_comment_url: String
    var contents_url: String
    var compare_url: String
    var merges_url: String
    var archive_url: String
    var downloads_url: String
    var issues_url: String
    var pulls_url: String
    var milestones_url: String
    var notifications_url: String
    var labels_url: String
    var releases_url: String
    var deployments_url: String
    var created_at: String
    var updated_at: String
    var pushed_at: String
    var git_url: String
    var ssh_url: String
    var clone_url: String
    var svn_url: String
    var homepage: Any?
    var size: Number
    var stargazers_count: Number
    var watchers_count: Number
    var language: String?
    var has_issues: Boolean
    var has_projects: Boolean
    var has_downloads: Boolean
    var has_wiki: Boolean
    var has_pages: Boolean
    var forks_count: Number
    var mirror_url: Any?
    var archived: Boolean
    var disabled: Boolean
    var open_issues_count: Number
    var license: Any?
    var forks: Number
    var open_issues: Number
    var watchers: Number
    var default_branch: String
    var allow_squash_merge: Boolean?
    var allow_merge_commit: Boolean?
    var allow_rebase_merge: Boolean?
    var delete_branch_on_merge: Boolean?
}

external interface WebhookPayloadPullRequestPullRequestBaseUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestPullRequestBase {
    var label: String
    var ref: String
    var sha: String
    var user: WebhookPayloadPullRequestPullRequestBaseUser
    var repo: WebhookPayloadPullRequestPullRequestBaseRepo
}

external interface WebhookPayloadPullRequestPullRequestHeadRepoOwner {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestPullRequestHeadRepo {
    var id: Number
    var node_id: String
    var name: String
    var full_name: String
    var private: Boolean
    var owner: WebhookPayloadPullRequestPullRequestHeadRepoOwner
    var html_url: String
    var description: Any?
    var fork: Boolean
    var url: String
    var forks_url: String
    var keys_url: String
    var collaborators_url: String
    var teams_url: String
    var hooks_url: String
    var issue_events_url: String
    var events_url: String
    var assignees_url: String
    var branches_url: String
    var tags_url: String
    var blobs_url: String
    var git_tags_url: String
    var git_refs_url: String
    var trees_url: String
    var statuses_url: String
    var languages_url: String
    var stargazers_url: String
    var contributors_url: String
    var subscribers_url: String
    var subscription_url: String
    var commits_url: String
    var git_commits_url: String
    var comments_url: String
    var issue_comment_url: String
    var contents_url: String
    var compare_url: String
    var merges_url: String
    var archive_url: String
    var downloads_url: String
    var issues_url: String
    var pulls_url: String
    var milestones_url: String
    var notifications_url: String
    var labels_url: String
    var releases_url: String
    var deployments_url: String
    var created_at: String
    var updated_at: String
    var pushed_at: String
    var git_url: String
    var ssh_url: String
    var clone_url: String
    var svn_url: String
    var homepage: Any?
    var size: Number
    var stargazers_count: Number
    var watchers_count: Number
    var language: String?
    var has_issues: Boolean
    var has_projects: Boolean
    var has_downloads: Boolean
    var has_wiki: Boolean
    var has_pages: Boolean
    var forks_count: Number
    var mirror_url: Any?
    var archived: Boolean
    var disabled: Boolean
    var open_issues_count: Number
    var license: Any?
    var forks: Number
    var open_issues: Number
    var watchers: Number
    var default_branch: String
    var allow_squash_merge: Boolean?
    var allow_merge_commit: Boolean?
    var allow_rebase_merge: Boolean?
    var delete_branch_on_merge: Boolean?
}

external interface WebhookPayloadPullRequestPullRequestHeadUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestPullRequestHead {
    var label: String
    var ref: String
    var sha: String
    var user: WebhookPayloadPullRequestPullRequestHeadUser
    var repo: WebhookPayloadPullRequestPullRequestHeadRepo
}

external interface WebhookPayloadPullRequestPullRequestUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPullRequestPullRequest {
    var url: String
    var id: Number
    var node_id: String
    var html_url: String
    var diff_url: String
    var patch_url: String
    var issue_url: String
    var number: Number
    var state: String
    var locked: Boolean
    var title: String
    var user: WebhookPayloadPullRequestPullRequestUser
    var body: String
    var created_at: String
    var updated_at: String
    var closed_at: String?
    var merged_at: Any?
    var merge_commit_sha: String?
    var assignee: `T$76`?
    var assignees: Array<WebhookPayloadPullRequestPullRequestAssigneesItem>
    var requested_reviewers: Array<Any>
    var requested_teams: Array<Any>
    var labels: Array<WebhookPayloadPullRequestPullRequestLabelsItem>
    var milestone: `T$75`?
    var commits_url: String
    var review_comments_url: String
    var review_comment_url: String
    var comments_url: String
    var statuses_url: String
    var head: WebhookPayloadPullRequestPullRequestHead
    var base: WebhookPayloadPullRequestPullRequestBase
    var _links: WebhookPayloadPullRequestPullRequestLinks
    var author_association: String
    var draft: Boolean
    var merged: Boolean
    var mergeable: Boolean?
    var rebaseable: Boolean?
    var mergeable_state: String
    var merged_by: Any?
    var comments: Number
    var review_comments: Number
    var maintainer_can_modify: Boolean
    var commits: Number
    var additions: Number
    var deletions: Number
    var changed_files: Number
}

external interface WebhookPayloadPullRequest {
    var action: String
    var number: Number
    var pull_request: WebhookPayloadPullRequestPullRequest
    var repository: PayloadRepository
    var sender: WebhookPayloadPullRequestSender
    var assignee: WebhookPayloadPullRequestAssignee?
    var installation: WebhookPayloadPullRequestInstallation?
    var label: WebhookPayloadPullRequestLabel?
}

external interface WebhookPayloadPublicSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPublic {
    var repository: PayloadRepository
    var sender: WebhookPayloadPublicSender
}

external interface WebhookPayloadProjectSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadProjectProjectCreator {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadProjectProject {
    var owner_url: String
    var url: String
    var html_url: String
    var columns_url: String
    var id: Number
    var node_id: String
    var name: String
    var body: String
    var number: Number
    var state: String
    var creator: WebhookPayloadProjectProjectCreator
    var created_at: String
    var updated_at: String
}

external interface WebhookPayloadProject {
    var action: String
    var project: WebhookPayloadProjectProject
    var repository: PayloadRepository
    var sender: WebhookPayloadProjectSender
}

external interface WebhookPayloadProjectColumnSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadProjectColumnProjectColumn {
    var url: String
    var project_url: String
    var cards_url: String
    var id: Number
    var node_id: String
    var name: String
    var created_at: String
    var updated_at: String
}

external interface WebhookPayloadProjectColumn {
    var action: String
    var project_column: WebhookPayloadProjectColumnProjectColumn
    var repository: PayloadRepository
    var sender: WebhookPayloadProjectColumnSender
}

external interface WebhookPayloadProjectCardSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadProjectCardProjectCardCreator {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadProjectCardProjectCard {
    var url: String
    var project_url: String
    var column_url: String
    var column_id: Number
    var id: Number
    var node_id: String
    var note: String
    var archived: Boolean
    var creator: WebhookPayloadProjectCardProjectCardCreator
    var created_at: String
    var updated_at: String
}

external interface WebhookPayloadProjectCard {
    var action: String
    var project_card: WebhookPayloadProjectCardProjectCard
    var repository: PayloadRepository
    var sender: WebhookPayloadProjectCardSender
}

external interface WebhookPayloadPingSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPingHookLastResponse {
    var code: Any?
    var status: String
    var message: Any?
}

external interface WebhookPayloadPingHookConfig {
    var content_type: String
    var url: String
    var insecure_ssl: String
}

external interface WebhookPayloadPingHook {
    var type: String
    var id: Number
    var name: String
    var active: Boolean
    var events: Array<String>
    var config: WebhookPayloadPingHookConfig
    var updated_at: String
    var created_at: String
    var url: String
    var test_url: String
    var ping_url: String
    var last_response: WebhookPayloadPingHookLastResponse
}

external interface WebhookPayloadPing {
    var zen: String
    var hook_id: Number
    var hook: WebhookPayloadPingHook
    var repository: PayloadRepository
    var sender: WebhookPayloadPingSender
}

external interface WebhookPayloadPageBuildSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPageBuildBuildPusher {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPageBuildBuildError {
    var message: Any?
}

external interface WebhookPayloadPageBuildBuild {
    var url: String
    var status: String
    var error: WebhookPayloadPageBuildBuildError
    var pusher: WebhookPayloadPageBuildBuildPusher
    var commit: String
    var duration: Number
    var created_at: String
    var updated_at: String
}

external interface WebhookPayloadPageBuild {
    var id: Number
    var build: WebhookPayloadPageBuildBuild
    var repository: PayloadRepository
    var sender: WebhookPayloadPageBuildSender
}

external interface WebhookPayloadPackageSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPackagePackageRegistry {
    var about_url: String
    var name: String
    var type: String
    var url: String
    var vendor: String
}

external interface WebhookPayloadPackagePackagePackageVersionAuthor {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPackagePackagePackageVersionPackageFilesItem {
    var download_url: String
    var id: Number
    var name: String
    var sha256: String
    var sha1: String
    var md5: String
    var content_type: String
    var state: String
    var size: Number
    var created_at: String
    var updated_at: String
}

external interface WebhookPayloadPackagePackagePackageVersionReleaseAuthor {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPackagePackagePackageVersionRelease {
    var url: String
    var html_url: String
    var id: Number
    var tag_name: String
    var target_commitish: String
    var name: String
    var draft: Boolean
    var author: WebhookPayloadPackagePackagePackageVersionReleaseAuthor
    var prerelease: Boolean
    var created_at: String
    var published_at: String
}

external interface WebhookPayloadPackagePackagePackageVersion {
    var id: Number
    var version: String
    var summary: String
    var body: String
    var body_html: String
    var release: WebhookPayloadPackagePackagePackageVersionRelease
    var manifest: String
    var html_url: String
    var tag_name: String
    var target_commitish: String
    var target_oid: String
    var draft: Boolean
    var prerelease: Boolean
    var created_at: String
    var updated_at: String
    var metadata: Array<Any>
    var package_files: Array<WebhookPayloadPackagePackagePackageVersionPackageFilesItem>
    var author: WebhookPayloadPackagePackagePackageVersionAuthor
    var installation_command: String
}

external interface WebhookPayloadPackagePackageOwner {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadPackagePackage {
    var id: Number
    var name: String
    var package_type: String
    var html_url: String
    var created_at: String
    var updated_at: String
    var owner: WebhookPayloadPackagePackageOwner
    var package_version: WebhookPayloadPackagePackagePackageVersion
    var registry: WebhookPayloadPackagePackageRegistry
}

external interface WebhookPayloadPackage {
    var action: String
    var `package`: WebhookPayloadPackagePackage
    var repository: PayloadRepository
    var sender: WebhookPayloadPackageSender
}

external interface WebhookPayloadOrgBlockSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadOrgBlockOrganization {
    var login: String
    var id: Number
    var node_id: String
    var url: String
    var repos_url: String
    var events_url: String
    var hooks_url: String
    var issues_url: String
    var members_url: String
    var public_members_url: String
    var avatar_url: String
    var description: String
}

external interface WebhookPayloadOrgBlockBlockedUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadOrgBlock {
    var action: String
    var blocked_user: WebhookPayloadOrgBlockBlockedUser
    var organization: WebhookPayloadOrgBlockOrganization
    var sender: WebhookPayloadOrgBlockSender
}

external interface WebhookPayloadOrganizationSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadOrganizationOrganization {
    var login: String
    var id: Number
    var node_id: String
    var url: String
    var repos_url: String
    var events_url: String
    var hooks_url: String
    var issues_url: String
    var members_url: String
    var public_members_url: String
    var avatar_url: String
    var description: String
}

external interface WebhookPayloadOrganizationMembershipUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadOrganizationMembership {
    var url: String
    var state: String
    var role: String
    var organization_url: String
    var user: WebhookPayloadOrganizationMembershipUser
}

external interface WebhookPayloadOrganization {
    var action: String
    var membership: WebhookPayloadOrganizationMembership
    var organization: WebhookPayloadOrganizationOrganization
    var sender: WebhookPayloadOrganizationSender
}

external interface WebhookPayloadMilestoneSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadMilestoneMilestoneCreator {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadMilestoneMilestone {
    var url: String
    var html_url: String
    var labels_url: String
    var id: Number
    var node_id: String
    var number: Number
    var title: String
    var description: String
    var creator: WebhookPayloadMilestoneMilestoneCreator
    var open_issues: Number
    var closed_issues: Number
    var state: String
    var created_at: String
    var updated_at: String
    var due_on: String
    var closed_at: String?
}

external interface WebhookPayloadMilestone {
    var action: String
    var milestone: WebhookPayloadMilestoneMilestone
    var repository: PayloadRepository
    var sender: WebhookPayloadMilestoneSender
}

external interface WebhookPayloadMetaSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadMetaHookConfig {
    var content_type: String
    var insecure_ssl: String
    var url: String
}

external interface WebhookPayloadMetaHook {
    var type: String
    var id: Number
    var name: String
    var active: Boolean
    var events: Array<String>
    var config: WebhookPayloadMetaHookConfig
    var updated_at: String
    var created_at: String
}

external interface WebhookPayloadMeta {
    var action: String
    var hook_id: Number
    var hook: WebhookPayloadMetaHook
    var repository: PayloadRepository
    var sender: WebhookPayloadMetaSender
}

external interface WebhookPayloadMembershipOrganization {
    var login: String
    var id: Number
    var node_id: String
    var url: String
    var repos_url: String
    var events_url: String
    var hooks_url: String
    var issues_url: String
    var members_url: String
    var public_members_url: String
    var avatar_url: String
    var description: String
}

external interface WebhookPayloadMembershipTeam {
    var name: String
    var id: Number
    var node_id: String
    var slug: String
    var description: String
    var privacy: String
    var url: String
    var html_url: String
    var members_url: String
    var repositories_url: String
    var permission: String
}

external interface WebhookPayloadMembershipSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadMembershipMember {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadMembership {
    var action: String
    var scope: String
    var member: WebhookPayloadMembershipMember
    var sender: WebhookPayloadMembershipSender
    var team: WebhookPayloadMembershipTeam
    var organization: WebhookPayloadMembershipOrganization
}

external interface WebhookPayloadMemberChangesPermission {
    var from: String
}

external interface WebhookPayloadMemberChanges {
    var permission: WebhookPayloadMemberChangesPermission
}

external interface WebhookPayloadMemberSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadMemberMember {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadMember {
    var action: String
    var member: WebhookPayloadMemberMember
    var repository: PayloadRepository
    var sender: WebhookPayloadMemberSender
    var changes: WebhookPayloadMemberChanges?
}

external interface WebhookPayloadMarketplacePurchasePreviousMarketplacePurchasePlan {
    var id: Number
    var name: String
    var description: String
    var monthly_price_in_cents: Number
    var yearly_price_in_cents: Number
    var price_model: String
    var has_free_trial: Boolean
    var unit_name: String
    var bullets: Array<String>
}

external interface WebhookPayloadMarketplacePurchasePreviousMarketplacePurchaseAccount {
    var type: String
    var id: Number
    var login: String
    var organization_billing_email: String
}

external interface WebhookPayloadMarketplacePurchasePreviousMarketplacePurchase {
    var account: WebhookPayloadMarketplacePurchasePreviousMarketplacePurchaseAccount
    var billing_cycle: String
    var on_free_trial: Boolean
    var free_trial_ends_on: Any?
    var unit_count: Number
    var plan: WebhookPayloadMarketplacePurchasePreviousMarketplacePurchasePlan
}

external interface WebhookPayloadMarketplacePurchaseMarketplacePurchasePlan {
    var id: Number
    var name: String
    var description: String
    var monthly_price_in_cents: Number
    var yearly_price_in_cents: Number
    var price_model: String
    var has_free_trial: Boolean
    var unit_name: String?
    var bullets: Array<String>
}

external interface WebhookPayloadMarketplacePurchaseMarketplacePurchaseAccount {
    var type: String
    var id: Number
    var login: String
    var organization_billing_email: String
}

external interface WebhookPayloadMarketplacePurchaseMarketplacePurchase {
    var account: WebhookPayloadMarketplacePurchaseMarketplacePurchaseAccount
    var billing_cycle: String
    var unit_count: Number
    var on_free_trial: Boolean
    var free_trial_ends_on: Any?
    var next_billing_date: String
    var plan: WebhookPayloadMarketplacePurchaseMarketplacePurchasePlan
}

external interface WebhookPayloadMarketplacePurchaseSender {
    var login: String
    var id: Number
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
    var email: String
}

external interface WebhookPayloadMarketplacePurchase {
    var action: String
    var effective_date: String
    var sender: WebhookPayloadMarketplacePurchaseSender
    var marketplace_purchase: WebhookPayloadMarketplacePurchaseMarketplacePurchase
    var previous_marketplace_purchase: WebhookPayloadMarketplacePurchasePreviousMarketplacePurchase?
}

external interface WebhookPayloadLabelChangesColor {
    var from: String
}

external interface WebhookPayloadLabelChanges {
    var color: WebhookPayloadLabelChangesColor
}

external interface WebhookPayloadLabelSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadLabelLabel {
    var id: Number
    var node_id: String
    var url: String
    var name: String
    var color: String
    var default: Boolean
}

external interface WebhookPayloadLabel {
    var action: String
    var label: WebhookPayloadLabelLabel
    var repository: PayloadRepository
    var sender: WebhookPayloadLabelSender
    var changes: WebhookPayloadLabelChanges?
}

external interface WebhookPayloadIssuesLabel {
    var id: Number
    var node_id: String
    var url: String
    var name: String
    var color: String
    var default: Boolean
}

external interface WebhookPayloadIssuesIssuePullRequest {
    var url: String
    var html_url: String
    var diff_url: String
    var patch_url: String
}

external interface WebhookPayloadIssuesAssignee {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadIssuesSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadIssuesChanges

external interface WebhookPayloadIssuesIssueMilestoneCreator {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface `T$77` {
    var url: String
    var html_url: String
    var labels_url: String
    var id: Number
    var node_id: String
    var number: Number
    var title: String
    var description: String
    var creator: WebhookPayloadIssuesIssueMilestoneCreator
    var open_issues: Number
    var closed_issues: Number
    var state: String
    var created_at: String
    var updated_at: String
    var due_on: String
    var closed_at: String
}

external interface WebhookPayloadIssuesIssueAssigneesItem {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadIssuesIssueLabelsItem {
    var id: Number
    var node_id: String
    var url: String
    var name: String
    var color: String
    var default: Boolean
}

external interface WebhookPayloadIssuesIssueUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadIssuesIssue {
    var url: String
    var repository_url: String
    var labels_url: String
    var comments_url: String
    var events_url: String
    var html_url: String
    var id: Number
    var node_id: String
    var number: Number
    var title: String
    var user: WebhookPayloadIssuesIssueUser
    var labels: Array<WebhookPayloadIssuesIssueLabelsItem>
    var state: String
    var locked: Boolean
    var assignee: `T$76`?
    var assignees: Array<WebhookPayloadIssuesIssueAssigneesItem>
    var milestone: `T$77`?
    var comments: Number
    var created_at: String
    var updated_at: String
    var closed_at: Any?
    var author_association: String
    var body: String
    var pull_request: WebhookPayloadIssuesIssuePullRequest?
}

external interface WebhookPayloadIssues {
    var action: String
    var issue: WebhookPayloadIssuesIssue
    var changes: WebhookPayloadIssuesChanges?
    var repository: PayloadRepository
    var sender: WebhookPayloadIssuesSender
    var assignee: WebhookPayloadIssuesAssignee?
    var label: WebhookPayloadIssuesLabel?
}

external interface WebhookPayloadIssueCommentChangesBody {
    var from: String
}

external interface WebhookPayloadIssueCommentChanges {
    var body: WebhookPayloadIssueCommentChangesBody
}

external interface WebhookPayloadIssueCommentSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadIssueCommentCommentUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadIssueCommentComment {
    var url: String
    var html_url: String
    var issue_url: String
    var id: Number
    var node_id: String
    var user: WebhookPayloadIssueCommentCommentUser
    var created_at: String
    var updated_at: String
    var author_association: String
    var body: String
}

external interface WebhookPayloadIssueCommentIssueMilestoneCreator {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadIssueCommentIssueMilestone {
    var url: String
    var html_url: String
    var labels_url: String
    var id: Number
    var node_id: String
    var number: Number
    var title: String
    var description: String
    var creator: WebhookPayloadIssueCommentIssueMilestoneCreator
    var open_issues: Number
    var closed_issues: Number
    var state: String
    var created_at: String
    var updated_at: String
    var due_on: String
    var closed_at: String
}

external interface WebhookPayloadIssueCommentIssueAssigneesItem {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadIssueCommentIssueAssignee {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadIssueCommentIssueLabelsItem {
    var id: Number
    var node_id: String
    var url: String
    var name: String
    var color: String
    var default: Boolean
}

external interface WebhookPayloadIssueCommentIssueUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadIssueCommentIssue {
    var url: String
    var repository_url: String
    var labels_url: String
    var comments_url: String
    var events_url: String
    var html_url: String
    var id: Number
    var node_id: String
    var number: Number
    var title: String
    var user: WebhookPayloadIssueCommentIssueUser
    var labels: Array<WebhookPayloadIssueCommentIssueLabelsItem>
    var state: String
    var locked: Boolean
    var assignee: WebhookPayloadIssueCommentIssueAssignee
    var assignees: Array<WebhookPayloadIssueCommentIssueAssigneesItem>
    var milestone: WebhookPayloadIssueCommentIssueMilestone
    var comments: Number
    var created_at: String
    var updated_at: String
    var closed_at: Any?
    var author_association: String
    var body: String
}

external interface WebhookPayloadIssueComment {
    var action: String
    var issue: WebhookPayloadIssueCommentIssue
    var comment: WebhookPayloadIssueCommentComment
    var repository: PayloadRepository
    var sender: WebhookPayloadIssueCommentSender
    var changes: WebhookPayloadIssueCommentChanges?
}

external interface WebhookPayloadInstallationRepositoriesRepositoriesRemovedItem {
    var id: Number
    var name: String
    var full_name: String
    var private: Boolean
}

external interface WebhookPayloadInstallationRepositoriesSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadInstallationRepositoriesRepositoriesAddedItem {
    var id: Number
    var node_id: String
    var name: String
    var full_name: String
    var private: Boolean
}

external interface WebhookPayloadInstallationRepositoriesInstallationPermissions {
    var administration: String?
    var statuses: String?
    var repository_projects: String?
    var repository_hooks: String?
    var pull_requests: String?
    var pages: String?
    var issues: String
    var deployments: String?
    var contents: String
    var checks: String?
    var metadata: String
    var vulnerability_alerts: String?
}

external interface WebhookPayloadInstallationRepositoriesInstallationAccount {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadInstallationRepositoriesInstallation {
    var id: Number
    var account: WebhookPayloadInstallationRepositoriesInstallationAccount
    var repository_selection: String
    var access_tokens_url: String
    var repositories_url: String
    var html_url: String
    var app_id: Number
    var target_id: Number
    var target_type: String
    var permissions: WebhookPayloadInstallationRepositoriesInstallationPermissions
    var events: Array<String>
    var created_at: Number
    var updated_at: Number
    var single_file_name: String?
}

external interface WebhookPayloadInstallationRepositories {
    var action: String
    var installation: WebhookPayloadInstallationRepositoriesInstallation
    var repository_selection: String
    var repositories_added: Array<WebhookPayloadInstallationRepositoriesRepositoriesAddedItem>
    var repositories_removed: Array<WebhookPayloadInstallationRepositoriesRepositoriesRemovedItem>
    var sender: WebhookPayloadInstallationRepositoriesSender
}

external interface WebhookPayloadInstallationSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadInstallationRepositoriesItem {
    var id: Number
    var node_id: String
    var name: String
    var full_name: String
    var private: Boolean
}

external interface WebhookPayloadInstallationInstallationPermissions {
    var metadata: String
    var contents: String
    var issues: String
    var administration: String?
    var checks: String?
    var deployments: String?
    var pages: String?
    var pull_requests: String?
    var repository_hooks: String?
    var repository_projects: String?
    var statuses: String?
    var vulnerability_alerts: String?
}

external interface WebhookPayloadInstallationInstallationAccount {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadInstallationInstallation {
    var id: Number
    var account: WebhookPayloadInstallationInstallationAccount
    var repository_selection: String
    var access_tokens_url: String
    var repositories_url: String
    var html_url: String
    var app_id: Number
    var target_id: Number
    var target_type: String
    var permissions: WebhookPayloadInstallationInstallationPermissions
    var events: Array<String>
    var created_at: Number
    var updated_at: Number
    var single_file_name: String?
}

external interface WebhookPayloadInstallation {
    var action: String
    var installation: WebhookPayloadInstallationInstallation
    var repositories: Array<WebhookPayloadInstallationRepositoriesItem>
    var sender: WebhookPayloadInstallationSender
}

external interface WebhookPayloadGollumSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadGollumPagesItem {
    var page_name: String
    var title: String
    var summary: Any?
    var action: String
    var sha: String
    var html_url: String
}

external interface WebhookPayloadGollum {
    var pages: Array<WebhookPayloadGollumPagesItem>
    var repository: PayloadRepository
    var sender: WebhookPayloadGollumSender
}

external interface WebhookPayloadGithubAppAuthorizationSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadGithubAppAuthorization {
    var action: String
    var sender: WebhookPayloadGithubAppAuthorizationSender
}

external interface WebhookPayloadForkSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadForkForkeeOwner {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadForkForkee {
    var id: Number
    var node_id: String
    var name: String
    var full_name: String
    var private: Boolean
    var owner: WebhookPayloadForkForkeeOwner
    var html_url: String
    var description: Any?
    var fork: Boolean
    var url: String
    var forks_url: String
    var keys_url: String
    var collaborators_url: String
    var teams_url: String
    var hooks_url: String
    var issue_events_url: String
    var events_url: String
    var assignees_url: String
    var branches_url: String
    var tags_url: String
    var blobs_url: String
    var git_tags_url: String
    var git_refs_url: String
    var trees_url: String
    var statuses_url: String
    var languages_url: String
    var stargazers_url: String
    var contributors_url: String
    var subscribers_url: String
    var subscription_url: String
    var commits_url: String
    var git_commits_url: String
    var comments_url: String
    var issue_comment_url: String
    var contents_url: String
    var compare_url: String
    var merges_url: String
    var archive_url: String
    var downloads_url: String
    var issues_url: String
    var pulls_url: String
    var milestones_url: String
    var notifications_url: String
    var labels_url: String
    var releases_url: String
    var deployments_url: String
    var created_at: String
    var updated_at: String
    var pushed_at: String
    var git_url: String
    var ssh_url: String
    var clone_url: String
    var svn_url: String
    var homepage: Any?
    var size: Number
    var stargazers_count: Number
    var watchers_count: Number
    var language: Any?
    var has_issues: Boolean
    var has_projects: Boolean
    var has_downloads: Boolean
    var has_wiki: Boolean
    var has_pages: Boolean
    var forks_count: Number
    var mirror_url: Any?
    var archived: Boolean
    var disabled: Boolean
    var open_issues_count: Number
    var license: Any?
    var forks: Number
    var open_issues: Number
    var watchers: Number
    var default_branch: String
    var public: Boolean
}

external interface WebhookPayloadFork {
    var forkee: WebhookPayloadForkForkee
    var repository: PayloadRepository
    var sender: WebhookPayloadForkSender
}

external interface WebhookPayloadDeploymentStatusSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadDeploymentStatusDeploymentCreator {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadDeploymentStatusDeploymentPayload

external interface WebhookPayloadDeploymentStatusDeployment {
    var url: String
    var id: Number
    var node_id: String
    var sha: String
    var ref: String
    var task: String
    var payload: WebhookPayloadDeploymentStatusDeploymentPayload
    var original_environment: String
    var environment: String
    var description: Any?
    var creator: WebhookPayloadDeploymentStatusDeploymentCreator
    var created_at: String
    var updated_at: String
    var statuses_url: String
    var repository_url: String
}

external interface WebhookPayloadDeploymentStatusDeploymentStatusCreator {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadDeploymentStatusDeploymentStatus {
    var url: String
    var id: Number
    var node_id: String
    var state: String
    var creator: WebhookPayloadDeploymentStatusDeploymentStatusCreator
    var description: String
    var environment: String
    var target_url: String
    var created_at: String
    var updated_at: String
    var deployment_url: String
    var repository_url: String
}

external interface WebhookPayloadDeploymentStatus {
    var action: String
    var deployment_status: WebhookPayloadDeploymentStatusDeploymentStatus
    var deployment: WebhookPayloadDeploymentStatusDeployment
    var repository: PayloadRepository
    var sender: WebhookPayloadDeploymentStatusSender
}

external interface WebhookPayloadDeploymentSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadDeploymentDeploymentCreator {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadDeploymentDeploymentPayload

external interface WebhookPayloadDeploymentDeployment {
    var url: String
    var id: Number
    var node_id: String
    var sha: String
    var ref: String
    var task: String
    var payload: WebhookPayloadDeploymentDeploymentPayload
    var original_environment: String
    var environment: String
    var description: Any?
    var creator: WebhookPayloadDeploymentDeploymentCreator
    var created_at: String
    var updated_at: String
    var statuses_url: String
    var repository_url: String
}

external interface WebhookPayloadDeployment {
    var action: String
    var deployment: WebhookPayloadDeploymentDeployment
    var repository: PayloadRepository
    var sender: WebhookPayloadDeploymentSender
}

external interface WebhookPayloadDeployKeySender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadDeployKeyKey {
    var id: Number
    var key: String
    var url: String
    var title: String
    var verified: Boolean
    var created_at: String
    var read_only: Boolean
}

external interface WebhookPayloadDeployKey {
    var action: String
    var key: WebhookPayloadDeployKeyKey
    var repository: PayloadRepository
    var sender: WebhookPayloadDeployKeySender
}

external interface WebhookPayloadDeleteSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadDelete {
    var ref: String
    var ref_type: String
    var pusher_type: String
    var repository: PayloadRepository
    var sender: WebhookPayloadDeleteSender
}

external interface WebhookPayloadCreateSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadCreate {
    var ref: String
    var ref_type: String
    var master_branch: String
    var description: Any?
    var pusher_type: String
    var repository: PayloadRepository
    var sender: WebhookPayloadCreateSender
}

external interface WebhookPayloadContentReferenceInstallation {
    var id: Number
    var node_id: String
}

external interface WebhookPayloadContentReferenceSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadContentReferenceContentReference {
    var id: Number
    var node_id: String
    var reference: String
}

external interface WebhookPayloadContentReference {
    var action: String
    var content_reference: WebhookPayloadContentReferenceContentReference
    var repository: PayloadRepository
    var sender: WebhookPayloadContentReferenceSender
    var installation: WebhookPayloadContentReferenceInstallation
}

external interface WebhookPayloadCommitCommentSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadCommitCommentCommentUser {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadCommitCommentComment {
    var url: String
    var html_url: String
    var id: Number
    var node_id: String
    var user: WebhookPayloadCommitCommentCommentUser
    var position: Any?
    var line: Any?
    var path: Any?
    var commit_id: String
    var created_at: String
    var updated_at: String
    var author_association: String
    var body: String
}

external interface WebhookPayloadCommitComment {
    var action: String
    var comment: WebhookPayloadCommitCommentComment
    var repository: PayloadRepository
    var sender: WebhookPayloadCommitCommentSender
}

external interface WebhookPayloadCheckSuiteInstallation {
    var id: Number
    var node_id: String
}

external interface WebhookPayloadCheckSuiteSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadCheckSuiteCheckSuiteHeadCommitCommitter {
    var name: String
    var email: String
}

external interface WebhookPayloadCheckSuiteCheckSuiteHeadCommitAuthor {
    var name: String
    var email: String
}

external interface WebhookPayloadCheckSuiteCheckSuiteHeadCommit {
    var id: String
    var tree_id: String
    var message: String
    var timestamp: String
    var author: WebhookPayloadCheckSuiteCheckSuiteHeadCommitAuthor
    var committer: WebhookPayloadCheckSuiteCheckSuiteHeadCommitCommitter
}

external interface WebhookPayloadCheckSuiteCheckSuiteAppPermissions {
    var administration: String
    var checks: String
    var contents: String
    var deployments: String
    var issues: String
    var members: String
    var metadata: String
    var organization_administration: String
    var organization_hooks: String
    var organization_plan: String
    var organization_projects: String
    var organization_user_blocking: String
    var pages: String
    var pull_requests: String
    var repository_hooks: String
    var repository_projects: String
    var statuses: String
    var team_discussions: String
    var vulnerability_alerts: String
}

external interface WebhookPayloadCheckSuiteCheckSuiteAppOwner {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadCheckSuiteCheckSuiteApp {
    var id: Number
    var node_id: String
    var owner: WebhookPayloadCheckSuiteCheckSuiteAppOwner
    var name: String
    var description: String
    var external_url: String
    var html_url: String
    var created_at: String
    var updated_at: String
    var permissions: WebhookPayloadCheckSuiteCheckSuiteAppPermissions
    var events: Array<Any>
}

external interface WebhookPayloadCheckSuiteCheckSuitePullRequestsItemBaseRepo {
    var id: Number
    var url: String
    var name: String
}

external interface WebhookPayloadCheckSuiteCheckSuitePullRequestsItemBase {
    var ref: String
    var sha: String
    var repo: WebhookPayloadCheckSuiteCheckSuitePullRequestsItemBaseRepo
}

external interface WebhookPayloadCheckSuiteCheckSuitePullRequestsItemHeadRepo {
    var id: Number
    var url: String
    var name: String
}

external interface WebhookPayloadCheckSuiteCheckSuitePullRequestsItemHead {
    var ref: String
    var sha: String
    var repo: WebhookPayloadCheckSuiteCheckSuitePullRequestsItemHeadRepo
}

external interface WebhookPayloadCheckSuiteCheckSuitePullRequestsItem {
    var url: String
    var id: Number
    var number: Number
    var head: WebhookPayloadCheckSuiteCheckSuitePullRequestsItemHead
    var base: WebhookPayloadCheckSuiteCheckSuitePullRequestsItemBase
}

external interface WebhookPayloadCheckSuiteCheckSuite {
    var id: Number
    var node_id: String
    var head_branch: String
    var head_sha: String
    var status: String
    var conclusion: String?
    var url: String
    var before: String
    var after: String
    var pull_requests: Array<WebhookPayloadCheckSuiteCheckSuitePullRequestsItem>
    var app: WebhookPayloadCheckSuiteCheckSuiteApp
    var created_at: String
    var updated_at: String
    var latest_check_runs_count: Number
    var check_runs_url: String
    var head_commit: WebhookPayloadCheckSuiteCheckSuiteHeadCommit
}

external interface WebhookPayloadCheckSuite {
    var action: String
    var check_suite: WebhookPayloadCheckSuiteCheckSuite
    var repository: PayloadRepository
    var sender: WebhookPayloadCheckSuiteSender
    var installation: WebhookPayloadCheckSuiteInstallation?
}

external interface WebhookPayloadCheckRunInstallation {
    var id: Number
}

external interface WebhookPayloadCheckRunOrganization {
    var login: String
    var id: Number
    var node_id: String
    var url: String
    var repos_url: String
    var events_url: String
    var hooks_url: String
    var issues_url: String
    var members_url: String
    var public_members_url: String
    var avatar_url: String
    var description: String
}

external interface WebhookPayloadCheckRunSender {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface PayloadRepositoryOwner {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
    var name: String?
    var email: String?
}

external interface PayloadRepository {
    var id: Number
    var node_id: String
    var name: String
    var full_name: String
    var private: Boolean
    var owner: PayloadRepositoryOwner
    var html_url: String
    var description: String?
    var fork: Boolean
    var url: String
    var forks_url: String
    var keys_url: String
    var collaborators_url: String
    var teams_url: String
    var hooks_url: String
    var issue_events_url: String
    var events_url: String
    var assignees_url: String
    var branches_url: String
    var tags_url: String
    var blobs_url: String
    var git_tags_url: String
    var git_refs_url: String
    var trees_url: String
    var statuses_url: String
    var languages_url: String
    var stargazers_url: String
    var contributors_url: String
    var subscribers_url: String
    var subscription_url: String
    var commits_url: String
    var git_commits_url: String
    var comments_url: String
    var issue_comment_url: String
    var contents_url: String
    var compare_url: String
    var merges_url: String
    var archive_url: String
    var downloads_url: String
    var issues_url: String
    var pulls_url: String
    var milestones_url: String
    var notifications_url: String
    var labels_url: String
    var releases_url: String
    var deployments_url: String
    var created_at: dynamic /* String | Number */
    var updated_at: String
    var pushed_at: dynamic /* String | Number */
    var git_url: String
    var ssh_url: String
    var clone_url: String
    var svn_url: String
    var homepage: String?
    var size: Number
    var stargazers_count: Number
    var watchers_count: Number
    var language: String?
    var has_issues: Boolean
    var has_projects: Boolean
    var has_downloads: Boolean
    var has_wiki: Boolean
    var has_pages: Boolean
    var forks_count: Number
    var mirror_url: Any?
    var archived: Boolean
    var disabled: Boolean?
    var open_issues_count: Number
    var license: Any?
    var forks: Number
    var open_issues: Number
    var watchers: Number
    var default_branch: String
    var stargazers: Number?
    var master_branch: String?
    var permissions: PayloadRepositoryPermissions?
}

external interface WebhookPayloadCheckRunCheckRunPullRequestsItemBaseRepo {
    var id: Number
    var url: String
    var name: String
}

external interface WebhookPayloadCheckRunCheckRunPullRequestsItemBase {
    var ref: String
    var sha: String
    var repo: WebhookPayloadCheckRunCheckRunPullRequestsItemBaseRepo
}

external interface WebhookPayloadCheckRunCheckRunPullRequestsItemHeadRepo {
    var id: Number
    var url: String
    var name: String
}

external interface WebhookPayloadCheckRunCheckRunPullRequestsItemHead {
    var ref: String
    var sha: String
    var repo: WebhookPayloadCheckRunCheckRunPullRequestsItemHeadRepo
}

external interface WebhookPayloadCheckRunCheckRunPullRequestsItem {
    var url: String
    var id: Number
    var number: Number
    var head: WebhookPayloadCheckRunCheckRunPullRequestsItemHead
    var base: WebhookPayloadCheckRunCheckRunPullRequestsItemBase
}

external interface WebhookPayloadCheckRunCheckRunAppPermissions {
    var administration: String
    var checks: String
    var contents: String
    var deployments: String
    var issues: String
    var members: String
    var metadata: String
    var organization_administration: String
    var organization_hooks: String
    var organization_plan: String
    var organization_projects: String
    var organization_user_blocking: String
    var pages: String
    var pull_requests: String
    var repository_hooks: String
    var repository_projects: String
    var statuses: String
    var team_discussions: String
    var vulnerability_alerts: String
}

external interface WebhookPayloadCheckRunCheckRunAppOwner {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadCheckRunCheckRunApp {
    var id: Number
    var node_id: String
    var owner: WebhookPayloadCheckRunCheckRunAppOwner
    var name: String
    var description: String?
    var external_url: String
    var html_url: String
    var created_at: String
    var updated_at: String
    var permissions: WebhookPayloadCheckRunCheckRunAppPermissions?
    var events: Array<Any>?
}

external interface WebhookPayloadCheckRunCheckRunCheckSuiteAppPermissions {
    var administration: String
    var checks: String
    var contents: String
    var deployments: String
    var issues: String
    var members: String
    var metadata: String
    var organization_administration: String
    var organization_hooks: String
    var organization_plan: String
    var organization_projects: String
    var organization_user_blocking: String
    var pages: String
    var pull_requests: String
    var repository_hooks: String
    var repository_projects: String
    var statuses: String
    var team_discussions: String
    var vulnerability_alerts: String
}

external interface WebhookPayloadCheckRunCheckRunCheckSuiteAppOwner {
    var login: String
    var id: Number
    var node_id: String
    var avatar_url: String
    var gravatar_id: String
    var url: String
    var html_url: String
    var followers_url: String
    var following_url: String
    var gists_url: String
    var starred_url: String
    var subscriptions_url: String
    var organizations_url: String
    var repos_url: String
    var events_url: String
    var received_events_url: String
    var type: String
    var site_admin: Boolean
}

external interface WebhookPayloadCheckRunCheckRunCheckSuiteApp {
    var id: Number
    var node_id: String
    var owner: WebhookPayloadCheckRunCheckRunCheckSuiteAppOwner
    var name: String
    var description: String?
    var external_url: String
    var html_url: String
    var created_at: String
    var updated_at: String
    var permissions: WebhookPayloadCheckRunCheckRunCheckSuiteAppPermissions?
    var events: Array<Any>?
}

external interface WebhookPayloadCheckRunCheckRunCheckSuitePullRequestsItemBaseRepo {
    var id: Number
    var url: String
    var name: String
}

external interface WebhookPayloadCheckRunCheckRunCheckSuitePullRequestsItemBase {
    var ref: String
    var sha: String
    var repo: WebhookPayloadCheckRunCheckRunCheckSuitePullRequestsItemBaseRepo
}

external interface WebhookPayloadCheckRunCheckRunCheckSuitePullRequestsItemHeadRepo {
    var id: Number
    var url: String
    var name: String
}

external interface WebhookPayloadCheckRunCheckRunCheckSuitePullRequestsItemHead {
    var ref: String
    var sha: String
    var repo: WebhookPayloadCheckRunCheckRunCheckSuitePullRequestsItemHeadRepo
}

external interface WebhookPayloadCheckRunCheckRunCheckSuitePullRequestsItem {
    var url: String
    var id: Number
    var number: Number
    var head: WebhookPayloadCheckRunCheckRunCheckSuitePullRequestsItemHead
    var base: WebhookPayloadCheckRunCheckRunCheckSuitePullRequestsItemBase
}

external interface WebhookPayloadCheckRunCheckRunCheckSuite {
    var id: Number
    var node_id: String?
    var head_branch: String
    var head_sha: String
    var status: String
    var conclusion: String?
    var url: String
    var before: String
    var after: String
    var pull_requests: Array<WebhookPayloadCheckRunCheckRunCheckSuitePullRequestsItem>
    var app: WebhookPayloadCheckRunCheckRunCheckSuiteApp
    var created_at: String
    var updated_at: String
}

external interface WebhookPayloadCheckRunCheckRunOutput {
    var title: String?
    var summary: String?
    var text: String?
    var annotations_count: Number
    var annotations_url: String
}

external interface WebhookPayloadCheckRunCheckRun {
    var id: Number
    var node_id: String?
    var head_sha: String
    var external_id: String
    var url: String
    var html_url: String
    var details_url: String?
    var status: String
    var conclusion: String?
    var started_at: String
    var completed_at: String?
    var output: WebhookPayloadCheckRunCheckRunOutput
    var name: String
    var check_suite: WebhookPayloadCheckRunCheckRunCheckSuite
    var app: WebhookPayloadCheckRunCheckRunApp
    var pull_requests: Array<WebhookPayloadCheckRunCheckRunPullRequestsItem>
}

external interface WebhookPayloadCheckRun {
    var action: String
    var check_run: WebhookPayloadCheckRunCheckRun
    var repository: PayloadRepository
    var sender: WebhookPayloadCheckRunSender
    var organization: WebhookPayloadCheckRunOrganization?
    var installation: WebhookPayloadCheckRunInstallation?
}
