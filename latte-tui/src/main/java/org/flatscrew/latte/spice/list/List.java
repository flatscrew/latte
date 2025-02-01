package org.flatscrew.latte.spice.list;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.ansi.Truncate;
import org.flatscrew.latte.cream.Position;
import org.flatscrew.latte.cream.Size;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.join.VerticalJoinDecorator;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;
import org.flatscrew.latte.spice.help.Help;
import org.flatscrew.latte.spice.help.KeyMap;
import org.flatscrew.latte.spice.key.Binding;
import org.flatscrew.latte.spice.list.fuzzy.FuzzyFilter;
import org.flatscrew.latte.spice.paginator.Bounds;
import org.flatscrew.latte.spice.paginator.Paginator;
import org.flatscrew.latte.spice.paginator.Type;
import org.flatscrew.latte.spice.spinner.Spinner;
import org.flatscrew.latte.spice.spinner.SpinnerType;
import org.flatscrew.latte.spice.spinner.TickMessage;
import org.flatscrew.latte.spice.textinput.TextInput;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.flatscrew.latte.Command.batch;
import static org.flatscrew.latte.spice.list.DefaultItemStyles.ELLIPSIS;

// FIXME it needs a complete overhaul, no idea how bubble guys maintained it o_O
public class List implements Model, KeyMap {

    private boolean showTitle;
    private boolean showFilter;
    private boolean showStatusBar;
    private boolean showPagination;
    private boolean showHelp;
    private boolean filteringEnabled;

    private String itemNameSingular;
    private String itemNamePlural;

    private String title;
    private Styles styles;
    private boolean infiniteScrolling;

    private Keys keys;
    private FilterFunction filterFunction;
    private boolean disableQuitKeybindings;

    private Supplier<Binding[]> additionalShortHelpKeys;
    private Supplier<Binding[]> additionalFullHelpKeys;

    private Spinner spinner;
    private boolean showSpinner;
    private int width;
    private int height;
    private Paginator paginator;
    private int cursor;
    private Help help;
    private TextInput filterInput;
    private FilterState filterState;

    private Duration statusMessageLifetime;
    private String statusMessage;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Future<? extends Message> statusMessageFuture;

    private java.util.List<Item> items;
    private java.util.List<FilteredItem> filteredItems;
    private ItemDelegate itemDelegate;

    public List(Item[] items, int width, int height) {
        this(items, new DefaultDelegate(), width, height);
    }

    public List(Item[] items, ItemDelegate delegate, int width, int height) {
        this.filterState = FilterState.Unfiltered;
        this.width = width;
        this.height = height;
        this.itemDelegate = delegate;
        this.items = java.util.List.of(items);
        this.filteredItems = new LinkedList<>();
        this.title = "List";
        this.showTitle = true;
        this.showFilter = true;
        this.showStatusBar = true;
        this.showPagination = true;
        this.showHelp = true;
        this.itemNameSingular = "item";
        this.itemNamePlural = "items";
        this.filteringEnabled = true;
        this.keys = new Keys();
        this.filterFunction = FuzzyFilter::defaultFilter;
        this.styles = Styles.defaultStyles();
        this.statusMessageLifetime = Duration.ofSeconds(1);
        this.statusMessage = "";
        this.help = new Help();

        this.spinner = new Spinner(SpinnerType.LINE);
        spinner.setStyle(styles.spinner());

        this.filterInput = new TextInput();
        filterInput.setPrompt("Filter: ");
        filterInput.setPromptStyle(styles.filterPrompt());
        filterInput.cursor().setStyle(styles.filterCursor());
        filterInput.setCharLimit(64);
        filterInput.focus();

        this.paginator = new Paginator();
        paginator.setType(Type.Dots);
    }

    public void setFilteringEnabled(boolean filteringEnabled) {
        this.filteringEnabled = filteringEnabled;
        if (!filteringEnabled) {
            resetFiltering();
        }
        updateKeybindings();
    }

    public boolean filteringEnabled() {
        return filteringEnabled;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
        updatePagination();
    }

    public void setFilterText(String filter) {
        this.filterState = FilterState.Filtering;
        this.filterInput.setValue(filter);

        Command cmd = filterItems();
        Message msg = cmd.execute();

        if (msg instanceof FilterMatchesMessage filterMatchesMessage) {
            this.filteredItems = filterMatchesMessage.filteredItems();
            this.filterState = FilterState.FilterApplied;
            this.paginator.setPage(0);
            this.cursor = 0;
            this.filterInput.cursorEnd();

            updatePagination();
            updateKeybindings();
        }
    }

    public void setFilterState(FilterState filterState) {
        this.paginator.setPage(0);
        this.cursor = 0;
        this.filterInput.cursorEnd();
        this.filterInput.focus();
        this.filterState = filterState;
    }

    public boolean showTitle() {
        return showTitle;
    }

    public void setShowFilter(boolean showFilter) {
        this.showFilter = showFilter;
        updatePagination();
    }

    public boolean showFilter() {
        return showFilter;
    }

    public void setShowStatusBar(boolean showStatusBar) {
        this.showStatusBar = showStatusBar;
        updatePagination();
    }

    public boolean showStatusBar() {
        return showStatusBar;
    }

    public void setStatusBarItemName(String singular, String plural) {
        this.itemNameSingular = singular;
        this.itemNamePlural = plural;
    }

    public String itemNameSingular() {
        return itemNameSingular;
    }

    public String itemNamePlural() {
        return itemNamePlural;
    }

    public void setShowPagination(boolean showPagination) {
        this.showPagination = showPagination;
        updatePagination();
    }

    public boolean showPagination() {
        return showPagination;
    }

    public void setShowHelp(boolean showHelp) {
        this.showHelp = showHelp;
        updatePagination();
    }

    public boolean showHelp() {
        return showHelp;
    }

    public java.util.List<Item> items() {
        return items;
    }

    public Command setItems(Item... items) {
        this.items = java.util.List.of(items);
        Command cmd = null;

        if (filterState != FilterState.Unfiltered) {
            this.filteredItems = null;
            cmd = filterItems();
        }

        updatePagination();
        updateKeybindings();
        return cmd;
    }

    public void select(int index) {
        this.paginator.setPage(index / paginator.perPage());
        this.cursor = index & paginator.perPage();
    }

    public void resetSelected() {
        select(0);
    }

    public void resetFilter() {
        resetFiltering();
    }

    public Command setItem(int index, Item item) {
        Command cmd = null;
        this.items.set(index, item);

        if (filterState != FilterState.Unfiltered) {
            cmd = filterItems();
        }

        updatePagination();
        return cmd;
    }

    public Command insertItem(int index, Item item) {
        Command cmd = null;
        this.items.add(index, item);

        if (filterState != FilterState.Unfiltered) {
            cmd = filterItems();
        }

        updatePagination();
        updateKeybindings();
        return cmd;
    }

    public void removeItem(int index) {
        this.items.remove(index);

        if (filterState != FilterState.Unfiltered) {
            this.filteredItems.remove(index);

            if (filteredItems.isEmpty()) {
                resetFiltering();
            }
        }
        updatePagination();
    }

    public void setItemDelegate(ItemDelegate itemDelegate) {
        this.itemDelegate = itemDelegate;
        updatePagination();
    }

    public java.util.List<Item> visibleItems() {
        if (filterState != FilterState.Unfiltered) {
            return filteredItems.stream().map(FilteredItem::item).toList();
        }
        return items;
    }

    public Item selectedItem() {
        java.util.List<Item> visibleItems = visibleItems();
        int index = index();

        if (index < 0 || visibleItems.isEmpty() || visibleItems.size() <= index) {
            return null;
        }
        return visibleItems.get(index);
    }

    public int[] matchesForItem(int index) {
        if (filteredItems == null || index >= filteredItems.size()) {
            return new int[0];
        }
        return filteredItems.get(index).matches();
    }

    public int index() {
        return paginator.page() * paginator.perPage() + cursor;
    }

    public int globalIndex() {
        int index = index();

        if (filteredItems == null || index >= filteredItems.size()) {
            return index;
        }

        return filteredItems.get(index).index();
    }

    public int cursor() {
        return cursor;
    }

    public void cursorUp() {
        this.cursor--;

        if (cursor < 0 && paginator.page() == 0) {
            if (infiniteScrolling) {
                paginator.setPage(paginator.totalPages() - 1);
                this.cursor = paginator.itemsOnPage(visibleItems().size()) - 1;
                return;
            }

            this.cursor = 0;
            return;
        }

        if (cursor >= 0) {
            return;
        }

        paginator.prevPage();
        this.cursor = paginator.itemsOnPage(visibleItems().size()) - 1;
    }

    public void cursorDown() {
        int itemsOnPage = paginator.itemsOnPage(visibleItems().size());
        this.cursor++;

        if (cursor < itemsOnPage) {
            return;
        }

        if (!paginator.onLastPage()) {
            paginator.nextPage();
            this.cursor = 0;
            return;
        }

        if (cursor > itemsOnPage) {
            this.cursor = 0;
            return;
        }

        this.cursor = itemsOnPage - 1;

        if (infiniteScrolling) {
            paginator.setPage(0);
            this.cursor = 0;
        }
    }

    public void nextPage() {
        paginator.nextPage();
    }

    public void prevPage() {
        paginator.prevPage();
    }

    public FilterState filterState() {
        return filterState;
    }

    public String filterValue() {
        return filterInput.value();
    }

    public boolean settingFilter() {
        return this.filterState == FilterState.Filtering;
    }

    public boolean isFiltered() {
        return this.filterState == FilterState.FilterApplied;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void setSpinnerType(SpinnerType spinnerType) {
        spinner.setType(spinnerType);
    }

    public Command toggleSpinner() {
        if (!showSpinner) {
            return startSpinner();
        }
        stopSpinner();
        return null;
    }

    public Command startSpinner() {
        this.showSpinner = true;
        return spinner::tick;
    }

    public void stopSpinner() {
        this.showSpinner = false;
    }

    public void disableQuitKeybindings() {
        this.disableQuitKeybindings = true;
        keys.quit().setEnabled(false);
        keys.forceQuit().setEnabled(false);
    }

    public Command newStatusMessage(String status) {
        this.statusMessage = status;

        if (statusMessageFuture != null && !statusMessageFuture.isDone()) {
            statusMessageFuture.cancel(true);
        }
        statusMessageFuture = scheduler.schedule(
                StatusMessageTimeoutMessage::new,
                statusMessageLifetime.toMillis(),
                TimeUnit.MILLISECONDS
        );

        return () -> {
            try {
                return statusMessageFuture.get();
            } catch (InterruptedException | CancellationException e) {
                return null;
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public void setStatusMessageLifetime(Duration statusMessageLifetime) {
        this.statusMessageLifetime = statusMessageLifetime;
    }

    public void setSize(int width, int height) {
        int promptWidth = Size.width(styles.title().render(filterInput.prompt()));

        this.width = width;
        this.height = height;
        this.help.setWidth(width);
        this.filterInput.setWidth(width - promptWidth - Size.width(spinnerView()));
        updatePagination();
    }

    public void setWidth(int width) {
        setSize(width, height);
    }

    public void setHeight(int height) {
        setSize(width, height);
    }

    private void resetFiltering() {
        if (filterState == FilterState.Unfiltered) {
            return;
        }

        this.filterState = FilterState.Unfiltered;
        this.filterInput.reset();
        this.filteredItems = null;

        updatePagination();
        updateKeybindings();
    }

    private java.util.List<FilteredItem> itemsAsFilteredItems() {
        return items.stream().map(FilteredItem::new).toList();
    }

    private void updateKeybindings() {
        if (filterState == FilterState.Filtering) {
            keys.cursorUp().setEnabled(false);
            keys.cursorDown().setEnabled(false);
            keys.nextPage().setEnabled(false);
            keys.prevPage().setEnabled(false);
            keys.goToStart().setEnabled(false);
            keys.goToEnd().setEnabled(false);
            keys.filter().setEnabled(false);
            keys.clearFilter().setEnabled(false);
            keys.cancelWhileFiltering().setEnabled(true);
            keys.acceptWhileFiltering().setEnabled(!"".equals(filterInput.value()));
            keys.quit().setEnabled(false);
            keys.showFullHelp().setEnabled(false);
            keys.closeFullHelp().setEnabled(false);
        } else {
            boolean hasItems = !items.isEmpty();
            keys.cursorUp().setEnabled(hasItems);
            keys.cursorDown().setEnabled(hasItems);
            keys.goToStart().setEnabled(hasItems);
            keys.goToEnd().setEnabled(hasItems);
            keys.filter().setEnabled(filteringEnabled && hasItems);

            boolean hasPages = paginator.totalPages() > 0;
            keys.nextPage().setEnabled(hasPages);
            keys.prevPage().setEnabled(hasPages);
            keys.clearFilter().setEnabled(filterState == FilterState.FilterApplied);
            keys.cancelWhileFiltering().setEnabled(false);
            keys.acceptWhileFiltering().setEnabled(false);
            keys.quit().setEnabled(!disableQuitKeybindings);

            if (help.showAll()) {
                keys.showFullHelp().setEnabled(true);
                keys.closeFullHelp().setEnabled(true);
            } else {
                boolean minHelp = countEnabledBindings(fullHelp()) > 1;
                keys.showFullHelp().setEnabled(minHelp);
                keys.closeFullHelp().setEnabled(minHelp);
            }
        }
    }

    private void updatePagination() {
        int index = index();
        int availHeight = this.height;

        if (showTitle || (showFilter && filteringEnabled)) {
            availHeight -= Size.height(titleView());
        }
        if (showStatusBar) {
            availHeight -= Size.height(statusView());
        }
        if (showPagination) {
            availHeight -= Size.height(paginationView());
        }
        if (showHelp) {
            availHeight -= Size.height(helpView());
        }

        paginator.setPerPage(Math.max(1, availHeight / (itemDelegate.height() + itemDelegate.spacing())));

        int pages = visibleItems().size();
        if (pages < 1) {
            paginator.setTotalPagesFromItemsSize(1);
        } else {
            paginator.setTotalPagesFromItemsSize(pages);
        }

        paginator.setPage(index / paginator.perPage());
        this.cursor = index % paginator.perPage();

        if (paginator.page() >= paginator.totalPages() - 1) {
            paginator.setPage(Math.max(0, paginator.totalPages() - 1));
        }
    }

    private void hideStatusMessage() {
        this.statusMessage = "";

        // Cancel the active timer if it's still running
        if (statusMessageFuture != null && !statusMessageFuture.isDone()) {
            statusMessageFuture.cancel(true);
        }
    }

    @Override
    public Command init() {
        paginator.activeDot(styles.activePaginationDot().render());
        paginator.inactiveDot(styles.inactivePaginationDot().render());

        updatePagination();
        updateKeybindings();

        return null;
    }

    @Override
    public UpdateResult<List> update(Message msg) {
        java.util.List<Command> commands = new LinkedList<>();

        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keys.forceQuit())) {
                return UpdateResult.from(this, QuitMessage::new);
            }
        } else if (msg instanceof FilterMatchesMessage filterMatchesMessage) {
            this.filteredItems = filterMatchesMessage.filteredItems();
            return UpdateResult.from(this);
        } else if (msg instanceof TickMessage tickMessage) {
            UpdateResult<Spinner> updateResult = spinner.update(msg);
            this.spinner = updateResult.model();
            if (showSpinner) {
                commands.add(updateResult.command());
            }
        } else if (msg instanceof StatusMessageTimeoutMessage timeoutMessage) {
            hideStatusMessage();
        }

        if (filterState == FilterState.Filtering) {
            commands.add(handleFiltering(msg));
        } else {
            commands.add(handleBrowsing(msg));
        }

        return UpdateResult.from(this, batch(commands));
    }

    private Command handleBrowsing(Message msg) {
        java.util.List<Command> commands = new LinkedList<>();

        int numItems = visibleItems().size();
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keys.clearFilter())) {
                resetFiltering();
            } else if (Binding.matches(keyPressMessage, keys.quit())) {
                return QuitMessage::new;
            } else if (Binding.matches(keyPressMessage, keys.cursorUp())) {
                cursorUp();
            } else if (Binding.matches(keyPressMessage, keys.cursorDown())) {
                cursorDown();
            } else if (Binding.matches(keyPressMessage, keys.prevPage())) {
                paginator.prevPage();
            } else if (Binding.matches(keyPressMessage, keys.nextPage())) {
                paginator.nextPage();
            } else if (Binding.matches(keyPressMessage, keys.goToStart())) {
                paginator.setPage(0);
                this.cursor = 0;
            } else if (Binding.matches(keyPressMessage, keys.goToEnd())) {
                paginator.setPage(paginator.totalPages() - 1);
                this.cursor = paginator.itemsOnPage(numItems) - 1;
            } else if (Binding.matches(keyPressMessage, keys.filter())) {
                hideStatusMessage();
                if ("".equals(filterInput.value())) {
                    this.filteredItems = itemsAsFilteredItems();
                }

                paginator.setPage(0);
                this.cursor = 0;
                this.filterState = FilterState.Filtering;
                filterInput.cursorEnd();
                filterInput.focus();
                updateKeybindings();
                return TextInput::blink;
            } else if (Binding.matches(keyPressMessage, keys.showFullHelp()) || Binding.matches(keyPressMessage, keys.closeFullHelp())) {
                help.setShowAll(!help.showAll());
                updatePagination();
            }

            Command cmd = itemDelegate.update(msg, this);
            commands.add(cmd);

            int itemsOnPage = paginator.itemsOnPage(visibleItems().size());
            if (cursor > itemsOnPage - 1) {
                this.cursor = Math.max(0, itemsOnPage - 1);
            }
        }

        return batch(commands);
    }

    private Command handleFiltering(Message msg) {
        java.util.List<Command> commands = new LinkedList<>();

        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keys.cancelWhileFiltering())) {
                resetFiltering();
                keys.filter().setEnabled(true);
                keys.clearFilter().setEnabled(false);
            } else if (Binding.matches(keyPressMessage, keys.acceptWhileFiltering())) {
                hideStatusMessage();

                if (!items.isEmpty()) {

                    java.util.List<Item> h = visibleItems();
                    if (!h.isEmpty()) {
                        filterInput.blur();
                        this.filterState = FilterState.FilterApplied;
                        updateKeybindings();

                        if (filterInput.isEmpty()) {
                            resetFiltering();
                        }

                    } else {
                        resetFiltering();
                    }

                }
            }
        }

        String beforeChange = filterInput.value();
        UpdateResult<TextInput> updateResult = filterInput.update(msg);
        boolean filterChanged = !Objects.equals(beforeChange, updateResult.model().value());
        this.filterInput = updateResult.model();
        commands.add(updateResult.command());

        if (filterChanged) {
            commands.add(filterItems());
            keys.acceptWhileFiltering().setEnabled(!filterInput.isEmpty());
        }
        updatePagination();
        return batch(commands);
    }

    @Override
    public String view() {
        java.util.List<String> sections = new ArrayList<>();
        int availHeight = this.height;

        if (showTitle || (showFilter && filteringEnabled)) {
            String v = titleView();
            sections.add(v);
            availHeight -= Size.height(v);
        }

        if (showStatusBar) {
            String v = statusView();
            sections.add(v);
            availHeight -= Size.height(v);
        }

        String pagination = null;
        if (showPagination) {
            pagination = paginationView();
            availHeight -= Size.height(pagination);
        }

        String help = null;
        if (showHelp) {
            help = helpView();
            availHeight -= Size.height(help);
        }

        String content = Style.newStyle().height(availHeight).render(populatedView());
        sections.add(content);

        if (showPagination) {
            sections.add(pagination);
        }

        if (showHelp) {
            sections.add(help);
        }

        return VerticalJoinDecorator.joinVertical(Position.Left, sections.toArray(new String[0]));
    }

    private String titleView() {
        StringBuilder view = new StringBuilder();
        Style titleBarStyle = styles.titleBar().copy();
        String spinnerView = spinnerView();
        int spinnerWidth = Size.width(spinnerView);
        String spinnerLeftGap = " ";
        boolean spinnerOnLeft = titleBarStyle.leftPadding() >= spinnerWidth + Size.width(spinnerLeftGap) && showSpinner;

        if (showFilter && filterState == FilterState.Filtering) {
            view.append(filterInput.view());
        } else if (showTitle) {
            if (showSpinner && spinnerOnLeft) {
                view.append(spinnerView).append(spinnerLeftGap);

                int titleBarGap = titleBarStyle.leftPadding();
                titleBarStyle = titleBarStyle.paddingLeft(titleBarGap - spinnerWidth - Size.width(spinnerLeftGap));
            }

            view.append(styles.title().render(title));

            if (filterState != FilterState.Filtering) {
                view.append(" ").append(statusMessage);
                view = new StringBuilder(Truncate.truncate(view.toString(), width - spinnerWidth, ELLIPSIS));
            }
        }

        if (showSpinner && !spinnerOnLeft) {
            int availSpace = width - Size.width(styles.titleBar().render(view.toString()));
            if (availSpace > spinnerWidth) {
                view
                        .append(" ".repeat(availSpace - spinnerWidth))
                        .append(spinnerView);
            }
        }

        if (!view.isEmpty()) {
            return titleBarStyle.render(view.toString());
        }
        return view.toString();
    }

    private String statusView() {
        StringBuilder status = new StringBuilder();
        int totalItems = items.size();
        int visibleItems = visibleItems().size();

        String itemName = itemNameSingular;
        if (visibleItems != 1) {
            itemName = itemNamePlural;
        }

        String itemsDisplay = "%d %s".formatted(visibleItems, itemName);

        if (filterState == FilterState.Filtering) {
            if (visibleItems == 0) {
                status = new StringBuilder(styles.statusEmpty().render("Nothing matched"));
            } else {
                status = new StringBuilder(itemsDisplay);
            }
        } else if (items.isEmpty()) {
            status = new StringBuilder(styles.statusEmpty().render("No " + itemNamePlural));
        } else {
            boolean filtered = filterState == FilterState.FilterApplied;

            if (filtered) {
                String f = filterInput.value().trim();
                f = Truncate.truncate(f, 10, ELLIPSIS);
                status.append("“%s” ".formatted(f));
            }

            status.append(itemsDisplay);
        }

        int numFiltered = totalItems - visibleItems;
        if (numFiltered > 0) {
            status
                    .append(styles.dividerDot().render())
                    .append(styles.statusBarFilterCount().render("%d filtered".formatted(numFiltered)));
        }

        return styles.statusBar().render(status.toString());
    }

    private String paginationView() {
        if (paginator.totalPages() < 2) {
            return "";
        }

        paginator.setType(Type.Dots);
        String s = paginator.view();
        if (Size.width(s) > width) {
            paginator.setType(Type.Arabic);
            s = styles.arabicPagination().render(paginator.view());
        }

        Style style = styles.paginationStyle().copy();
        if (itemDelegate.spacing() == 0 && style.topMargin() == 0) {
            style = style.marginBottom(1);
        }

        return style.render(s);
    }

    private String populatedView() {
        java.util.List<Item> items = visibleItems();

        StringBuilder b = new StringBuilder();

        if (items.isEmpty()) {
            if (filterState == FilterState.Filtering) {
                return "";
            }
            return styles.noItems().render("No " + itemNamePlural + ".");
        } else {
            Bounds sliceBounds = paginator.getSliceBounds(items.size());
            int start = sliceBounds.start();
            int end = sliceBounds.end();

            java.util.List<Item> docs = items.subList(start, end);
            for (int i = 0; i < docs.size(); i++) {
                itemDelegate.render(b, this, i + start, docs.get(i));
                if (i != end - 1) {
                    b.append("\n".repeat(itemDelegate.spacing() + 1));
                }
            }
        }

        int itemsOnPage = paginator.itemsOnPage(items.size());
        if (itemsOnPage < paginator.perPage()) {
            int n = (paginator.perPage() - itemsOnPage) * (itemDelegate.height() + itemDelegate.spacing());
            // can this happen at all ??
            if (items.isEmpty()) {
                n -= itemDelegate.height() - 1;
            }
            b.append("\n".repeat(n));
        }

        return b.toString();
    }

    private String helpView() {
        return styles.helpStyle().render(help.render(this));
    }

    private String spinnerView() {
        return spinner.view();
    }

    private Command filterItems() {
        return () -> {
            if ("".equals(filterInput.value()) || filterState == FilterState.Unfiltered) {
                return new FilterMatchesMessage(itemsAsFilteredItems());
            }

            String[] targets = new String[items.size()];

            for (int i = 0; i < items.size(); i++) {
                targets[i] = items.get(i).filterValue();
            }

            java.util.List<FilteredItem> filterMatches = new LinkedList<>();

            Rank[] ranks = filterFunction.apply(filterInput.value(), targets);
            for (Rank rank : ranks) {
                filterMatches.add(new FilteredItem(
                        rank.getIndex(),
                        items.get(rank.getIndex()),
                        rank.getMatchedIndexes()
                ));
            }
            return new FilterMatchesMessage(filterMatches);
        };
    }

    private int countEnabledBindings(Binding[][] groups) {
        int count = 0;
        for (Binding[] group : groups) {
            for (Binding binding : group) {
                if (binding.isEnabled()) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public Binding[] shortHelp() {
        java.util.List<Binding> kb = new LinkedList<>(Arrays.asList(
                keys.cursorUp(),
                keys.cursorDown()
        ));

        boolean filtering = filterState == FilterState.Filtering;
        if (!filtering) {
            if (itemDelegate instanceof KeyMap delegateKeyMap) {
                kb.addAll(Arrays.asList(delegateKeyMap.shortHelp()));
            }
        }

        kb.addAll(Arrays.asList(
                keys.filter(),
                keys.clearFilter(),
                keys.acceptWhileFiltering(),
                keys.cancelWhileFiltering()
        ));

        if (!filtering && additionalFullHelpKeys != null) {
            kb.addAll(Arrays.asList(additionalFullHelpKeys.get()));
        }

        kb.addAll(Arrays.asList(
                keys.quit(),
                keys.showFullHelp()
        ));

        return kb.toArray(new Binding[0]);
    }

    @Override
    public Binding[][] fullHelp() {
        java.util.List<Binding[]> kb = new LinkedList<>();
        kb.add(new Binding[]{
                keys.cursorUp(),
                keys.cursorDown(),
                keys.nextPage(),
                keys.prevPage(),
                keys.goToStart(),
                keys.goToEnd()
        });

        boolean filtering = filterState == FilterState.Filtering;
        if (!filtering) {
            if (itemDelegate instanceof KeyMap delegateKeyMap) {
                kb.addAll(Arrays.asList(delegateKeyMap.fullHelp()));
            }
        }

        java.util.List<Binding> listLevelBindings = new LinkedList<>(Arrays.asList(
                keys.filter(),
                keys.clearFilter(),
                keys.acceptWhileFiltering(),
                keys.cancelWhileFiltering()
        ));

        if (!filtering && additionalFullHelpKeys != null) {
            listLevelBindings.addAll(Arrays.asList(additionalFullHelpKeys.get()));
        }

        kb.add(listLevelBindings.toArray(new Binding[0]));
        kb.add(new Binding[]{
                keys.quit(),
                keys.closeFullHelp()
        });
        return kb.toArray(new Binding[0][]);
    }

    public java.util.List<FilteredItem> filteredItems() {
        return filteredItems;
    }
}
