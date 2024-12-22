# Porting status
This page covers the current status of porting Bubble Tea to Latte.

## Examples
Table presents an overall status of porting code available under **examples** directory of Bubble Tea codebase. The idea is to rewrite all the code samples and enhance Latte logic with missing parts found during implementation.

Each entry below represents a single example code fragment and the status of its migration into Java equivalent which is represented by one of the labels:

`⚪ TODO` No effort have been put into this one yet 

`🟡 In Progress` It's currently under development, and it might be some notable changes will be applied to Latte logic to conform it

`🟢 Done` It's done like DONE, the example works 100% the same as original code does.

`🔴 Won't do` It won't be done because of some technical limitations or was done some other way with explanation in **Notes**.

I'll rewrite the table to match your legend formatting. Here's the table with the new emoji-based status indicators:

| Example  | Status  | Notes
|-----------------| ------- | --------
| altscreen-toggle | `🟢 Done` |
| autocomplete | `⚪ TODO` |
| cellbuffer | `⚪ TODO` |
| chat | `⚪ TODO` |
| composable-views | `⚪ TODO` |
| credit-card-form | `⚪ TODO` |
| debounce | `⚪ TODO` |
| exec | `⚪ TODO` |
| file-picker | `⚪ TODO` |
| focus-blur | `🟢 Done` |
| fullscreen | `🟢 Done` |
| glamour | `⚪ TODO` |
| help | `⚪ TODO` |
| http | `🟢 Done` |
| list-default | `⚪ TODO` |
| list-fancy | `⚪ TODO` |
| list-simple | `⚪ TODO` |
| mouse | `🟢 Done` |
| package-manager | `⚪ TODO` |
| pager | `⚪ TODO` |
| paginator | `⚪ TODO` |
| pipe | `⚪ TODO` |
| prevent-quit | `⚪ TODO` |
| progress-animated | `⚪ TODO` |
| progress-download | `⚪ TODO` |
| progress-static | `⚪ TODO` |
| realtime | `⚪ TODO` |
| result | `🔴 Won't do` | Rewritten as **demo** example.
| send-msg | `⚪ TODO` |
| sequence | `🟡 In Progress` | Nested sequences and batches are not supported yet,<br>needs to be revisited as golang implementation feels odd.
| set-window-title | `🟢 Done` |
| simple | `⚪ TODO` |
| spinner | `🟢 Done` |
| spinners | `⚪ TODO` |
| split-editors | `⚪ TODO` |
| stopwatch | `⚪ TODO` |
| suspend | `⚪ TODO` |
| table-resize | `⚪ TODO` |
| table | `⚪ TODO` |
| tabs | `⚪ TODO` |
| textarea | `⚪ TODO` |
| textinput | `⚪ TODO` |
| textinputs | `⚪ TODO` |
| timer | `⚪ TODO` |
| tui-daemon-combo | `⚪ TODO` |
| views | `⚪ TODO` |
| window-size | `🟢 Done` |