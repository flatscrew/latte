# Porting status
This page covers the current status of porting Bubble Tea to Latte.

## Examples
Table presents an overall status of porting code available under **examples** directory of Bubble Tea codebase. The idea is to rewrite all the code samples and enhance Latte logic with missing parts found during implementation.

Each entry below represents a single example code fragment and the status of it's migration into Java equivalent which is represented by one of the labels:

<span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> No effort have been put into this one yet 

<span style="background-color: #f9c74f; color: black; padding: 2px 6px; border-radius: 3px;">In Progress</span> It's currently under development and it might be some notable changes will be applied to Latte logic to conform it

<span style="background-color: #90be6d; color: white; padding: 2px 6px; border-radius: 3px;">Done</span> It's done like DONE, the example works 100% the same as original code does.

<span style="background-color: #d73a4a; color: white; padding: 2px 6px; border-radius: 3px;">Won't do</span> It won't be done because of some technical limiations or was done some other way with explanation in **Notes**.

| Example  | Status  | Notes
| -----------------| ------- | --------
| altscreen-toggle | <span style="background-color: #90be6d; color: white; padding: 2px 6px; border-radius: 3px;">Done</span> |
| autocomplete | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| cellbuffer | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| chat | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| composable-views | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| credit-card-form | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| debounce | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| exec | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| file-picker | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| focus-blur | <span style="background-color: #90be6d; color: white; padding: 2px 6px; border-radius: 3px;">Done</span> |
| fullscreen | <span style="background-color: #90be6d; color: white; padding: 2px 6px; border-radius: 3px;">Done</span> |
| glamour | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| help | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| http | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| list-default | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| list-fancy | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| list-simple | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| mouse | <span style="background-color: #90be6d; color: white; padding: 2px 6px; border-radius: 3px;">Done</span> |
| package-manager | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| pager | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| paginator | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| pipe | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| prevent-quit | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| progress-animated | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| progress-download | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| progress-static | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| realtime | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| result | <span style="background-color: #d73a4a; color: white; padding: 2px 6px; border-radius: 3px;">Won't do</span> | Rewritten as **demo** example.
| send-msg | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| sequence | <span style="background-color: #f9c74f; color: black; padding: 2px 6px; border-radius: 3px;">In Progress</span> | Nested sequences and batches are not supported yet,<br>needs to be revisited as golang implementation feels odd.
| set-window-title | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| simple | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| spinner | <span style="background-color: #90be6d; color: white; padding: 2px 6px; border-radius: 3px;">Done</span> |
| spinners | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| split-editors | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| stopwatch | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| suspend | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| table-resize | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| table | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| tabs | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| textarea | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| textinput | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| textinputs | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| timer | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| tui-daemon-combo | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| views | <span style="background-color: #6e7681; color: white; padding: 2px 6px; border-radius: 3px;">TODO</span> |
| window-size | <span style="background-color: #90be6d; color: white; padding: 2px 6px; border-radius: 3px;">Done</span> |